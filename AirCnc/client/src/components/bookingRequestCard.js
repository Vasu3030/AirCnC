import React from "react";
import { Link } from "react-router-dom";

const token = localStorage.getItem("token");

export default function BookingRequestCard(props) {
	return (
		<div className="bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700 flex flex-row">
			<div className="p-5 m-20 w-full">
				<div className="flex flex-row justify-between items-center">
					<Link to={`/address/${props.booking.addressId}`}>
						<h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
							{props.booking.name}
						</h5>
					</Link>
					{props.booking.status == "pending" ? (
						<span className="bg-yellow-100 text-yellow-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-yellow-900 dark:text-yellow-300">
							{props.booking.status}
						</span>
					) : null}
					{props.booking.status == "accepted" ? (
						<span className="bg-green-100 text-green-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-yellow-900 dark:text-yellow-300">
							{props.booking.status}
						</span>
					) : null}
					{props.booking.status == "ended" ? (
						<span className="bg-gray-100 text-gray-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-yellow-900 dark:text-yellow-300">
							{props.booking.status}
						</span>
					) : null}
					{props.booking.status == "progress" ? (
						<span className="bg-blue-100 text-blue-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-yellow-900 dark:text-yellow-300">
							{props.booking.status}
						</span>
					) : null}
				</div>
				<p className="mb-3 font-normal text-gray-700 dark:text-gray-400">
					From:{" "}
					<span className="font-semibold">
						{new Date(props.booking.from).toLocaleDateString("fr-FR")}
					</span>
				</p>
				<p className="mb-3 font-normal text-gray-700 dark:text-gray-400">
					To:{" "}
					<span className="font-semibold">
						{new Date(props.booking.to).toLocaleDateString("fr-FR")}
					</span>
				</p>
				<p className="mb-3 font-normal text-gray-700 dark:text-gray-400">
					Cost: <span className="font-semibold">{props.booking.price} $</span>
				</p>
				<p className="mb-3 font-normal text-gray-700 dark:text-gray-400">
					User for:{" "}
					<Link to={`/user/${props.booking.userId}`}>
						<span className="font-semibold text-blue-700">
							{props.booking.userName}
						</span>
					</Link>
				</p>
				{props.booking.status == "pending" ? (
					<div>
						<button
							onClick={() => {
								props.acceptBooking(token, props.booking.id, props.action);
							}}
							type="button"
							className="focus:outline-none text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-900"
						>
							Accept
						</button>
						<button
							onClick={() => {
								props.cancelBooking(token, props.booking.id, props.action);
							}}
							type="button"
							className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
						>
							Decline
						</button>
					</div>
				) : null}
				{props.booking.status == "accepted" ? (
					<div>
						<button
							onClick={() => {
								props.cancelBooking(token, props.booking.id, props.action);
							}}
							type="button"
							className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
						>
							Cancel
						</button>
					</div>
				) : null}
			</div>
		</div>
	);
}
