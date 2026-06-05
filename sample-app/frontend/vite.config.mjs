import { defineConfig } from "vite";
import { resolve } from "node:path";

export default defineConfig({
  define: {
    "process.env.NODE_ENV": JSON.stringify("production"),
  },
  resolve: {
    alias: {
      "@mareano-frontend": resolve("../../../mareano-frontend/src"),
    },
  },
  esbuild: {
    jsx: "transform",
    jsxFactory: "React.createElement",
    jsxFragment: "React.Fragment",
  },
  build: {
    outDir: "dist",
    emptyOutDir: true,
    lib: {
      entry: resolve("src/main.tsx"),
      formats: ["es"],
      fileName: () => "sample-extension.js",
    },
    rollupOptions: {
      output: {
        assetFileNames: "assets/[name]-[hash][extname]",
      },
    },
  },
});
