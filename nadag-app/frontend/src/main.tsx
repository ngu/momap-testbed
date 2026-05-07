export {};

import type {
	FrontendContribution,
	FrontendExtension,
} from "../../../../mareano-frontend/src/extensions/frontendExtensionsRegistry";
import { createNadagSearchRightMenuToolContribution } from "./nadag-search/rightMenuTool";
import { createNadagSearchRightMenuActivityContribution } from "./nadag-search/rightMenuActivity";

declare global {
	interface Window {
		__frontendExtensions?: FrontendExtension[];
		__frontendContributions?: FrontendContribution[];
		__registerFrontendExtension?: (
			extension: FrontendExtension,
		) => void;
	}
}

const nadagFrontendExtension: FrontendExtension = ({
	React,
	useRightMenuActivity,
}) => {
	return [
		createNadagSearchRightMenuToolContribution({
			React,
			useRightMenuActivity,
		}),
		createNadagSearchRightMenuActivityContribution({
			React,
			useRightMenuActivity,
		}),
	];
};

if (window.__registerFrontendExtension) {
	window.__registerFrontendExtension(nadagFrontendExtension);
} else {
	window.__frontendExtensions = [
		...(window.__frontendExtensions ?? []),
		nadagFrontendExtension,
	];
}

window.dispatchEvent(new CustomEvent("nadag-extension:loaded"));
