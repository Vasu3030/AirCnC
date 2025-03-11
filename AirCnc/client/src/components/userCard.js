import React from "react";
import { useNavigate, Link } from "react-router-dom";
import { deleteUser } from "../services/user";

export default function UserCard(props) {
	const navigate = useNavigate();
	const userId = localStorage.getItem("id");
	const role = localStorage.getItem("role");
	const token = localStorage.getItem("token");

	const delAccount = () => {
		deleteUser(token, props.user.id, (err, res, status) => {
			if (status === 200) {
				if (props.user.id == userId) {
					handleLogout();
					window.location.href = "/login";
				} else {
					navigate(-1);
				}
			} else {
				console.log(status, err);
			}
		});
	};

	const handleLogout = () => {
		localStorage.removeItem("token");
		localStorage.removeItem("id");
		localStorage.removeItem("role");
		localStorage.removeItem("username");
	};

	return (
		<div className="w-full max-w-sm bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700">
			<div className="flex flex-col items-center pb-10 pt-10">
				<h5 className="mb-1 text-xl font-medium text-gray-900 dark:text-white">
					<Link to={`/user/${props.user.id}`}>{props.user.username}</Link>
				</h5>
				<span className="text-sm text-gray-500 dark:text-gray-400">
					{props.user.role}
				</span>
				<h5 className="mb-1 text-l font-medium text-gray-900 dark:text-white">
					Number of goods: {props.addresses ? props.addresses.length : null}
				</h5>
				{userId == props.user.id || role == "ROLE_ADMIN" ? (
					<div className="flex mt-4 space-x-3 md:mt-6">
						<button
							onClick={() => {
								delAccount();
							}}
							className="inline-flex items-center px-3 py-2 text-sm font-medium text-center text-white bg-red-700 rounded-lg hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
						>
							Delete account
						</button>
					</div>
				) : null}
			</div>
		</div>
	);
}
