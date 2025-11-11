const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const config = require('./config/default');

const app = express();

// Логирование входящих запросов
app.use((req, res, next) => {
  console.log(`[${new Date().toISOString()}] ${req.method} ${req.originalUrl}`);
  next();
});

// Функция для определения целевого сервиса на основе пути
function getTargetService(req) {
  const path = req.originalUrl;

  // Проверяем пути для events service
  if (config.routes.events.some(route => path.startsWith(route))) {
    return {
      url: config.services.events.url,
      name: 'events',
      weight: 100
    };
  }

  // Проверяем пути для movies service
  if (config.routes.movies.some(route => path.startsWith(route))) {
    if (!config.gradualMigration) {
      // Если фиче-флаг выключен, весь трафик идет в monolith
      return {
        url: config.services.monolith.url,
        name: 'monolith',
        weight: 100
      };
    }

    // Распределение трафика между monolith и movies service
    const random = Math.random() * 100;
    if (random < config.services.movies.weight) {
      return {
        url: config.services.movies.url,
        name: 'movies',
        weight: config.services.movies.weight
      };
    } else {
      return {
        url: config.services.monolith.url,
        name: 'monolith',
        weight: config.services.monolith.weight
      };
    }
  }

  // Все остальные запросы идут в monolith
  return {
    url: config.services.monolith.url,
    name: 'monolith',
    weight: 100
  };
}

// Health check endpoint
app.get('/health', (req, res) => {
  res.status(200).json({
    status: 'OK',
    service: 'traffic-proxy',
    timestamp: new Date().toISOString(),
    config: {
      gradualMigration: config.gradualMigration,
      trafficDistribution: {
        monolith: config.services.monolith.weight,
        movies: config.services.movies.weight,
        events: config.services.events.weight
      }
    }
  });
});

// Config endpoint для проверки текущей конфигурации
app.get('/config', (req, res) => {
  res.json({
    gradualMigration: config.gradualMigration,
    trafficDistribution: {
      monolith: `${config.services.monolith.weight}%`,
      movies: `${config.services.movies.weight}%`,
      events: `${config.services.events.weight}%`
    },
    routes: config.routes
  });
});

// Основной proxy middleware
app.use('*', (req, res) => {
  const targetService = getTargetService(req);

  console.log(`Routing to: ${targetService.name} (${targetService.weight}%) - ${targetService.url}${req.originalUrl}`);

  const proxy = createProxyMiddleware({
    target: targetService.url,
    changeOrigin: true,
    pathRewrite: {
      '^/api/v1': '/api/v1' // Можно настроить перезапись путей при необходимости
    },
    onProxyReq: (proxyReq, req, res) => {
      // Добавляем заголовки для отслеживания
      proxyReq.setHeader('X-Proxied-By', 'traffic-distribution-proxy');
      proxyReq.setHeader('X-Target-Service', targetService.name);
    },
    onProxyRes: (proxyRes, req, res) => {
      console.log(`[${new Date().toISOString()}] ${req.method} ${req.originalUrl} -> ${targetService.name} [${proxyRes.statusCode}]`);
    },
    onError: (err, req, res) => {
      console.error(`Proxy error for ${targetService.name}:`, err.message);
      res.status(503).json({
        error: 'Service temporarily unavailable',
        message: `Cannot connect to ${targetService.name} service`,
        service: targetService.name
      });
    }
  });

  proxy(req, res);
});

// Обработка ошибок
app.use((err, req, res, next) => {
  console.error('Unhandled error:', err);
  res.status(500).json({
    error: 'Internal server error',
    message: 'Something went wrong in the proxy service'
  });
});

// Запуск сервера
const PORT = config.port;
app.listen(PORT, '0.0.0.0', () => {
  console.log('🚀 Traffic Distribution Proxy Service started');
  console.log(`📍 Port: ${PORT}`);
  console.log(`🔄 Gradual Migration: ${config.gradualMigration}`);
  console.log('📊 Traffic Distribution:');
  console.log(`   - Monolith: ${config.services.monolith.weight}%`);
  console.log(`   - Movies Service: ${config.services.movies.weight}%`);
  console.log(`   - Events Service: ${config.services.events.weight}%`);
  console.log('🛣️  Route Mapping:');
  console.log(`   - Movies routes: ${config.routes.movies.join(', ')}`);
  console.log(`   - Events routes: ${config.routes.events.join(', ')}`);
  console.log('⏱️  Ready to proxy requests...');
});