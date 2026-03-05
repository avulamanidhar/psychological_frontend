/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'bg-splash': '#F0F4FF',
        'title-blue': '#1A237E',
        'desc-gray': '#757575',
        'button-blue': '#4A90E2',
        'stroke-gray': '#E0E0E0',
        'complete-teal': '#80CBC4',
        'meditation-purple': '#7E57C2',
        'breathing-teal': '#4DB6AC',
        'grounding-bg': '#E8EAF6',
      },
    },
  },
  plugins: [],
}
