import React from "react";
import AddressForm from "../components/addressForm";

export default function AddressCreate(props) {
	return (
		<div className="flex flex-col justify-center items-center mt-5">
			<h2 className="text-4xl font-bold dark:text-white text-blue-500">
				Add a new good
			</h2>

			<AddressForm action={"create"} />
		</div>
	);
}
