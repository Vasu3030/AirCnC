import { useEffect, useState } from "react";
import AddressCard from "../components/addressCard";
import { getAddressByUserId } from "../services/address";
import { Link } from "react-router-dom";

export default function AddressManaging() {
	const [addresses, setAddresses] = useState();

	useEffect(() => {
		const id = localStorage.getItem("id");
		getAddressByUserId(id, (err, res, status) => {
			if (status === 200) {
				setAddresses(res);
			} else {
				console.log(status, err);
			}
		});
	}, []);

	if (addresses) {
		return (
			<div className="mx-auto max-w-screen-xl">
				<div className="flex justify-end my-8">
					<Link
						to={`/address/create`}
						className="text-blue-700 hover:text-white border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center mr-2 mb-2 dark:border-blue-500 dark:text-blue-500 dark:hover:text-white dark:hover:bg-blue-500 dark:focus:ring-blue-800"
					>
						Add an address
					</Link>
				</div>
				<div
					className="grid grid-cols-1 md:grid-cols-3 gap-5"
					style={{
						marginBottom: 100,
					}}
				>
					{addresses.map((address) => (
						<AddressCard key={address.id} address={address} />
					))}
				</div>
			</div>
		);
	} else {
		return (
			<div className="font-bold flex justify-center items-center text-xl mt-80">
				<h1>You don't have good yet</h1>
			</div>
		);
	}
}
