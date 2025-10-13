FROM node:18-alpine

WORKDIR /app

# Копируем package.json и package-lock.json
COPY package*.json ./

# Устанавливаем зависимости
RUN npm install --production

# Копируем исходный код
COPY . .

# Создаем не-root пользователя для безопасности
RUN addgroup -g 1001 -S nodejs
RUN adduser -S proxyuser -u 1001

# Меняем владельца файлов
RUN chown -R proxyuser:nodejs /app
USER proxyuser

# Открываем порт
EXPOSE 8000

# Запускаем приложение
CMD ["npm", "start"]