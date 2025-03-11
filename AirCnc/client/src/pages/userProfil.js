import { useEffect, useState } from "react";
import UserCard from "../components/userCard";
import AddressCard from "../components/addressCard";
import { getAddressByUserId } from "../services/address";
import { getUserById } from "../services/user";
import { useParams } from "react-router-dom";

export default function UserProfil() {
	const { id } = useParams();
	const [addresses, setAddresses] = useState();
	const [user, setUser] = useState();

	useEffect(() => {
		getUserById(id, (err, res, status) => {
			if (status === 200) {
				setUser(res);
			} else {
				console.log(status, err);
			}
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
			<div className="flex flex-col justify-center items-center">
				<UserCard user={user} setUser={setUser} addresses={addresses} />
				<div className="flex flex-row m-10 justify-evenly w-full">
					{addresses.map((address) => (
						<AddressCard key={address.id} address={address} />
					))}
				</div>
			</div>
		);
	}
}
