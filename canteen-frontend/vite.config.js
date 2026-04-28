import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

const proxyTarget = process.env.VITE_API_PROXY_TARGET || 'http://localhost:8081'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/api/v1': {
        target: proxyTarget,
        changeOrigin: true
      }
    }
  }
})
