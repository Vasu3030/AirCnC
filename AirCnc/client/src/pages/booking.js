import { useEffect, useState } from "react";
import {
	getBookingByUserId,
	deleteBooking,
	getBookingByUserIdAndStatus,
	updateBooking,
	getBookingByUserIdIncoming,
} from "../services/booking";
import BookingCard from "../components/bookingCard";
import BookingRequestCard from "../components/bookingRequestCard";

export default function Booking(props) {
	const [bookings, setBookings] = useState();
	const token = localStorage.getItem("token");
	const id = localStorage.getItem("id");
	const [action, setAction] = useState();

	useEffect(() => {
		if (id) {
			getBookingByUserId(token, id, (err, res, status) => {
				if (status === 200) {
					setBookings(res);
					setAction("default");
				} else {
					console.log(status, err);
				}
			});
		}
	}, []);

	const cancelBooking = (myToken, bookingId, action) => {
		deleteBooking(token, bookingId, (err, res, status) => {
			if (status === 200) {
				if (id) {
					console.log(action);
					if (action == "default") {
						getBookingByUserId(token, id, (err, res, status) => {
							if (status === 200) {
								setBookings(res);
								setAction(action);
							} else {
								console.log(status, err);
							}
						});
					} else {
						getBookingByUserIdAndStatus(
							token,
							id,
							"pending",
							(err, res, status) => {
								if (status === 200) {
									setBookings(res);
									setAction(action);
								} else {
									console.log(status, err);
								}
							}
						);
					}
				}
			} else {
				console.log(status, err);
			}
		});
	};

	const acceptBooking = (myToken, bookingId, action) => {
		updateBooking(token, bookingId, true, (err, res, status) => {
			if (status === 200) {
				if (id) {
					getBookingByUserIdAndStatus(
						token,
						id,
						"pending",
						(err, res, status) => {
							if (status === 200) {
								setBookings(res);
								setAction(action);
							} else {
								console.log(status, err);
							}
						}
					);
				}
			} else {
				console.log(status, err);
			}
		});
	};

	const getMyBookings = () => {
		if (id) {
			getBookingByUserId(token, id, (err, res, status) => {
				if (status === 200) {
					setBookings(res);
					setAction("default");
				} else {
					console.log(status, err);
				}
			});
		}
	};

	const getMyRequest = (bookingStatus, action) => {
		if (id) {
			getBookingByUserIdAndStatus(
				token,
				id,
				bookingStatus,
				(err, res, status) => {
					if (status === 200) {
						setBookings(res);
						setAction(action);
					} else {
						console.log(status, err);
					}
				}
			);
		}
	};

	const getMyIncoming = (action) => {
		if (id) {
			getBookingByUserIdIncoming(token, id, (err, res, status) => {
				if (status === 200) {
					setBookings(res);
					setAction(action);
				} else {
					console.log(status, err);
				}
			});
		}
	};
	if (bookings) {
		return (
			<div className="mx-auto max-w-screen-xl mt-5">
				{/* <p className="tracking-tighter text-gray-500 md:text-lg dark:text-gray-400 ml-3">Number of results : {bookings.length}</p> */}

				<div className="inline-flex rounded-md shadow-sm">
					<button
						onClick={() => {
							getMyBookings();
						}}
						aria-current="page"
						className="px-4 py-2 text-sm font-medium  text-gray-900 bg-white border border-gray-200 rounded-l-lg hover:bg-gray-100 focus:z-10 focus:ring-2 focus:ring-blue-700 focus:text-blue-700 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-blue-500 dark:focus:text-white"
					>
						My bookings
					</button>
					<button
						onClick={() => {
							getMyRequest("pending", "request");
						}}
						className="px-4 py-2 text-sm font-medium text-gray-900 bg-white border-t border-b border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-2 focus:ring-blue-700 focus:text-blue-700 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-blue-500 dark:focus:text-white"
					>
						Request for my goods
					</button>
					<button
						onClick={() => {
							getMyIncoming("accepted");
						}}
						className="px-4 py-2 text-sm font-medium text-gray-900 bg-white border border-gray-200 rounded-r-md hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-2 focus:ring-blue-700 focus:text-blue-700 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-blue-500 dark:focus:text-white"
					>
						My incoming goods
					</button>
				</div>

				<div className="mt-5 flex flex-col gap-10">
					{action == "default"
						? bookings.map((booking) => (
								<BookingCard
									key={booking.id}
									booking={booking}
									cancelBooking={cancelBooking}
								/>
						  ))
						: null}

					{action == "request"
						? bookings.map((booking) => (
								<BookingRequestCard
									key={booking.id}
									booking={booking}
									cancelBooking={cancelBooking}
									acceptBooking={acceptBooking}
									action={"request"}
								/>
						  ))
						: null}

					{action == "accepted"
						? bookings.map((booking) => (
								<BookingRequestCard
									key={booking.id}
									booking={booking}
									cancelBooking={cancelBooking}
									action={"accepted"}
								/>
						  ))
						: null}
				</div>
			</div>
		);
	}
}
