export {};

import type { FrontendContribution } from "../../../../mareano-frontend/src/extensions/frontendExtensionsRegistry";

declare global {
  interface Window {
		__frontendContributions?: FrontendContribution[];
  }
}

const contribution: FrontendContribution = {
	id: "nadag.nadag-search",
	purpose: "right-menu-tool",
	meta: {
		name: "Nadag search",
	},
	factory: ({ React }) => {
		return function NadagSearchButton() {
			const [isOpen, setIsOpen] = React.useState(false);

			const icon = React.createElement(
				"svg",
				{
					viewBox: "0 0 256 256",
					width: "22",
					height: "22",
					"aria-hidden": "true",
					fill: "currentColor",
				},
				React.createElement("path", {
					d: "M229.66,218.34,179.28,168a92.12,92.12,0,1,0-11.31,11.31l50.38,50.35a8,8,0,0,0,11.31-11.32ZM44,108a64,64,0,1,1,64,64A64.07,64.07,0,0,1,44,108Z",
				}),
			);

			return React.createElement(
				"button",
				{
					type: "button",
					onClick: () => setIsOpen((prev) => !prev),
					title: "Nadag search",
					"aria-label": isOpen ? "Close Nadag search" : "Open Nadag search",
					"aria-expanded": isOpen,
					className: [
						"flex h-13 w-13 cursor-pointer items-center justify-center rounded-full border-none",
						isOpen ? "bg-brand-link text-black" : "bg-brand-primary text-white",
					].join(" "),
				},
				icon,
			);
		};
	},
};

console.log("Registering frontend contribution:", contribution);
window.__frontendContributions = window.__frontendContributions ?? [];
window.__frontendContributions.push(contribution);
window.dispatchEvent(new CustomEvent("nadag-extension:loaded"));
