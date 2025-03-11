import { useEffect, useState } from "react";
import UserCard from "../components/userCard";
import AddressCard from "../components/addressCard";
import { getAddressByUserId } from "../services/address";
export default function Profil() {
	const username = localStorage.getItem("username");
	const role = localStorage.getItem("role");
	const id = localStorage.getItem("id");
	const [addresses, setAddresses] = useState();
	const [user, setUser] = useState();

	useEffect(() => {
		setUser({
			username: username,
			role: role,
			id: id,
		});
		getAddressByUserId(id, (err, res, status) => {
			if (status === 200) {
				setAddresses(res);
			} else {
				console.log(status, err);
			}
		});
	}, []);

	if (user && addresses) {
		return (
			<div className="flex flex-col justify-center items-center mt-5">
				<UserCard user={user} setUser={setUser} addresses={addresses} />
				<div className="mt-8 grid grid-cols-1 md:grid-cols-3">
					{addresses.map((address) => (
						<AddressCard key={address.id} address={address} />
					))}
				</div>
			</div>
		);
	}
}
