module.exports = {
  port: process.env.PORT || 8000,
  gradualMigration: process.env.GRADUAL_MIGRATION === "true",
  services: {
    monolith: {
      url: process.env.MONOLITH_URL || 'http://localhost:8080',
      weight: 100 - (parseInt(process.env.MOVIES_MIGRATION_PERCENT) || 0)
    },
    movies: {
      url: process.env.MOVIES_SERVICE_URL || 'http://localhost:8081',
      weight: parseInt(process.env.MOVIES_MIGRATION_PERCENT) || 0
    },
    events: {
      url: process.env.EVENTS_SERVICE_URL || 'http://localhost:8082',
      weight: 100 // events service gets 100% of events traffic
    }
  },
  routes: {
    // Определяем какие пути куда направлять
    movies: ['/api/movies'],
    events: ['/api/events']
  }
};