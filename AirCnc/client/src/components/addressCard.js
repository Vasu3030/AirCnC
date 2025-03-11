import React from "react";
import { useNavigate, Link } from "react-router-dom";

export default function AddressCard(props) {
	const userId = localStorage.getItem("id");

	return (
		<Link
			to={
				userId != props.address.userId
					? `/address/${props.address.id}`
					: `/address/${props.address.id}`
			}
		>
			<div className="flex flex-col justify-between w-full h-full bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-100">
				{props.address.pictureDetails[0] ? (
					<img
						className="rounded-t-lg max-h-56 w-auto"
						src={props.address.pictureDetails[0].url}
						alt="/"
					/>
				) : (
					<img
						className="rounded-t-lg max-h-44"
						src="https://us.123rf.com/450wm/mattbadal/mattbadal1911/mattbadal191100006/135029891-missing-picture-page-for-website-design-or-mobile-app-design-no-image-available-icon-vector.jpg?ver=6"
						alt="/"
					/>
				)}
				<div className="p-5">
					<h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
						<Link to={`/address/${props.address.id}`}>
							{props.address.name}
						</Link>
					</h5>
					<p className="mb-3 font-normal text-gray-700 dark:text-gray-400">
						<strong>{props.address.price} $</strong> / night
					</p>
					{userId != props.address.userId ? (
						<p className="mb-3 font-normal text-gray-700 dark:text-gray-400">
							owner:{" "}
							<Link to={`/user/${props.address.userId}`}>
								<strong className="text-blue-700">
									{props.address.username}
								</strong>
							</Link>
						</p>
					) : null}
				</div>
			</div>
		</Link>
	);
}
