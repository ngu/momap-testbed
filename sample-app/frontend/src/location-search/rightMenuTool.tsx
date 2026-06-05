import type {
	FrontendContribution,
	FrontendHostApi,
} from "@mareano-frontend/extensions/frontendExtensionsRegistry";
import { LOCATION_SEARCH_ID } from "./geojson";

const searchIconPath =
	"M229.66,218.34,179.28,168a92.12,92.12,0,1,0-11.31,11.31l50.38,50.35a8,8,0,0,0,11.31-11.32ZM44,108a64,64,0,1,1,64,64A64.07,64.07,0,0,1,44,108Z";

export function createLocationSearchRightMenuToolContribution({
	React,
	useRightMenuActivity,
}: FrontendHostApi): FrontendContribution {
	return {
		id: LOCATION_SEARCH_ID,
		purpose: "right-menu-tool",
		meta: {
			name: "Location search",
		},
		factory: () => {
			return function LocationSearchToolButton() {
				const { isActive: isOpen, toggle } = useRightMenuActivity(LOCATION_SEARCH_ID);

				return (
					<button
						type="button"
						onClick={toggle}
						title="Location search"
						aria-label={isOpen ? "Close Location search" : "Open Location search"}
						aria-expanded={isOpen}
						className={[
							"flex h-13 w-13 cursor-pointer items-center justify-center rounded-full border-none",
							isOpen ? "bg-brand-link text-black" : "bg-brand-primary text-white",
						].join(" ")}
					>
						<svg
							viewBox="0 0 256 256"
							width="22"
							height="22"
							aria-hidden="true"
							fill="currentColor"
						>
							<path d={searchIconPath} />
						</svg>
					</button>
				);
			};
		},
	};
}