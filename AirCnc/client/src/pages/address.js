import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import {
	getAddressById,
	deletePicture,
	addPicture,
	deleteAddress,
} from "../services/address";
import BookForm from "../components/bookForm";
import AddressForm from "../components/addressForm";
import CommentList from "../components/commentList";

export default function Address(props) {
	const navigate = useNavigate();
	const [address, setAddresse] = useState();
	const [url, setUrl] = useState();
	const { id } = useParams();
	const userId = localStorage.getItem("id");
	const role = localStorage.getItem("role");
	const [showComments, setShowComments] = useState();
	const [commsLabel, setCommsLabel] = useState("Comments");

	useEffect(() => {
		getAddressById(id, (err, res, status) => {
			if (status === 200) {
				setAddresse(res);
			} else {
				console.log(status, err);
			}
		});
	}, []);

	const deletePic = (id, addressId) => {
		const token = localStorage.getItem("token");
		deletePicture(token, id, (err, res, status) => {
			if (status === 200) {
				getAddressById(addressId, (err, res, status) => {
					if (status === 200) {
						setAddresse(res);
					} else {
						console.log(status, err);
					}
				});
			} else {
				console.log(status, err);
			}
		});
	};

	const addPic = (addressId) => {
		const token = localStorage.getItem("token");
		addPicture(token, addressId, url, (err, res, status) => {
			if (status === 201) {
				getAddressById(addressId, (err, res, status) => {
					if (status === 200) {
						setAddresse(res);
					} else {
						console.log(status, err);
					}
				});
			} else {
				console.log(status, err);
			}
		});
	};

	const deleteGood = () => {
		const token = localStorage.getItem("token");
		deleteAddress(token, address.id, (err, res, status) => {
			if (status === 200) {
				navigate(-1);
			} else {
				console.log(status, err);
			}
		});
	};

	if (address) {
		return (
			<div
				className="max-w-screen-xl mx-auto my-5"
				style={{
					// display: "flex",
					// flexDirection: "column",
					// justifyContent: "space-evenly",
					// alignItems: "center",
					// gap: "20px",
					marginTop: 10,
					// marginBottom: 100,
				}}
			>
				{/* <button
					onClick={() => {
						navigate(-1);
					}}
					className="inline-flex items-center px-3 py-2 text-sm font-medium text-center text-white bg-blue-700 rounded-lg hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
				>
					Back
				</button> */}
				<div className="inline-flex rounded-md shadow-sm my-5">
					<button
						onClick={() => {
							if (!showComments) {
								setShowComments(true);
								setCommsLabel("Details");
							} else {
								setShowComments(false);
								setCommsLabel("Comments");
							}
						}}
						aria-current="page"
						className="px-4 py-2 text-sm font-medium  text-gray-900 bg-white border border-gray-200 rounded-l-lg hover:bg-gray-100 focus:z-10 focus:ring-2 focus:ring-blue-700 focus:text-blue-700 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-blue-500 dark:focus:text-white"
					>
						{commsLabel}
					</button>
				</div>

				{showComments ? (
					<CommentList
						address={address}
						isAuthenticated={props.isAuthenticated}
					/>
				) : null}

				{!showComments && (
					<>
						{(props.isAuthenticated && userId == address.userId) ||
						role == "ROLE_ADMIN" ? (
							<div className="flex flex-row w-full justify-around items-center my-5">
								<label
									className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
									htmlFor="picture"
								>
									Add picture
								</label>
								<input
									as="text"
									placeholder="Paste URL"
									className="w-4/5 bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block pl-5 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
									id="picture"
									name="picture"
									onChange={(event) => {
										setUrl(event.target.value);
									}}
								/>
								<button
									onClick={() => {
										addPic(address.id);
									}}
									className="inline-flex items-center px-5 py-3 text-sm font-medium text-center text-white bg-blue-700 rounded-lg hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
								>
									Add
								</button>
							</div>
						) : null}

						<div className="grid grid-cols-2 md:grid-cols-3 gap-8">
							{address.pictureDetails.map((picture) => (
								<div
									key={picture.id}
									className="relative overflow-hidden rounded-lg"
									style={{ width: "400px", height: "250px" }}
								>
									<img
										className="absolute inset-0 object-cover"
										src={picture.url}
										alt=""
									/>
									{(props.isAuthenticated && userId == address.userId) ||
									role == "ROLE_ADMIN" ? (
										<div
											onClick={() => deletePic(picture.id, picture.addressId)}
											className="absolute top-0 right-0 p-2 bg-red-500 text-white rounded hover:bg-red-700 hover:cursor-pointer"
										>
											<span>Delete</span>
										</div>
									) : null}
								</div>
							))}
						</div>

						<div className="mt-5">
							<p className="font-bold text-2xl">{address.name}</p>
							<p className="">
								location: {address.street}, {address.city} -{" "}
								{address.postalCode}, {address.country}
							</p>
						</div>
						{userId != address.userId ? (
							<p className="">
								Host:{" "}
								<Link to={`/user/${address.userId}`}>
									<span className="text-blue-700">{address.username}</span>
								</Link>
							</p>
						) : (
							<p className="text-gray-500 dark:text-gray-400">
								{address.price}$ / night
							</p>
						)}
						{props.isAuthenticated && userId != address.userId ? (
							<BookForm address={address} userId={userId} />
						) : null}
						{props.isAuthenticated &&
						(userId == address.userId || role == "ROLE_ADMIN") ? (
							<AddressForm
								address={address}
								setAddresse={setAddresse}
								action={"edit"}
							/>
						) : null}
						{props.isAuthenticated &&
						(userId == address.userId || role == "ROLE_ADMIN") ? (
							<button
								onClick={() => {
									deleteGood();
								}}
								className="inline-flex items-center px-3 py-2 text-sm font-medium text-center text-white bg-red-700 rounded-lg hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
							>
								Delete this good
							</button>
						) : null}
					</>
				)}
			</div>
		);
	} else {
		return <div>Address not found</div>;
	}
}
