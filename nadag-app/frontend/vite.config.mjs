import { defineConfig } from "vite";
import { resolve } from "node:path";

export default defineConfig({
  define: {
    "process.env.NODE_ENV": JSON.stringify("production"),
  },
  build: {
    outDir: "dist",
    emptyOutDir: true,
    lib: {
      entry: resolve("src/main.tsx"),
      formats: ["es"],
      fileName: () => "nadag-extension.js",
    },
    rollupOptions: {
      output: {
        assetFileNames: "assets/[name]-[hash][extname]",
      },
    },
  },
});
