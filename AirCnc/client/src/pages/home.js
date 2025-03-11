import { getAllAddress } from "../services/address";
import { useEffect, useState } from "react";
import AddressCard from "../components/addressCard";

export default function Home() {
	const [addresses, setAddresses] = useState();

	useEffect(() => {
		const token = localStorage.getItem("token");
		getAllAddress(token, (err, res, status) => {
			if (status === 200) {
				setAddresses(res);
			} else {
				console.log(status, err);
			}
		});
	}, []);

	if (addresses?.length > 0) {
		return (
			<div className="mt-8">
				<div
					className="mx-auto max-w-screen-xl grid grid-cols-1 md:grid-cols-3 gap-5"
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
				<h1>No addresses available</h1>
			</div>
		);
	}
}
