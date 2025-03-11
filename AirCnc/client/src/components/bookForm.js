import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Formik, Field, Form } from "formik";
import { bookAddress } from "../services/booking";

export default function BookForm(props) {
	const navigate = useNavigate();
	const [priceChange, setPriceChange] = useState(0);
	const initialValues = {
		fromDate: "",
		toDate: "",
		price: props.address.price,
	};

	const handleChange = (event, values, setFieldValue) => {
		const { name, value } = event.target;
		setFieldValue(name, value);
		if (name === "fromDate" || name === "toDate") {
			const fromDate = name === "fromDate" ? value : values.fromDate;
			const toDate = name === "toDate" ? value : values.toDate;
			const price = props.address.price * getDaysBetweenDates(toDate, fromDate);
			setFieldValue("price", price);
			setPriceChange(price);
		}
	};

	const onSubmit = (values) => {
		const token = localStorage.getItem("token");
		bookAddress(
			token,
			values.fromDate,
			values.toDate,
			props.address.id,
			values.price,
			(error, res, status) => {
				if (error) {
					alert(error.message);
				} else if (status === 201) {
					navigate("/booking");
				} else {
					alert(res.message);
				}
			}
		);
	};

	const getDaysBetweenDates = (date1, date2) => {
		const convertedDate1 = new Date(date1);
		const convertedDate2 = new Date(date2);

		const timeDifference = Math.abs(convertedDate2 - convertedDate1);
		const daysDifference = Math.ceil(timeDifference / (1000 * 60 * 60 * 24));

		return daysDifference;
	};

	return (
		<div className="my-8 p-10 flex flex-row border border-gray-300 rounded-lg w-2/5">
			<Formik initialValues={initialValues} onSubmit={onSubmit}>
				{({ values, setFieldValue }) => (
					<Form className="w-full px-5">
						<div className="flex justify-between w-full">
							<div>
								<label
									className="block mb-2 text-sm font-medium"
									htmlFor="fromDate"
								>
									From
								</label>
								<Field
									type="date"
									className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
									id="fromDate"
									name="fromDate"
									onChange={(event) =>
										handleChange(event, values, setFieldValue)
									}
								/>
							</div>
							<div>
								<label
									className="block mb-2 text-sm font-medium"
									htmlFor="toDate"
								>
									To
								</label>
								<Field
									type="date"
									className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
									id="toDate"
									name="toDate"
									onChange={(event) =>
										handleChange(event, values, setFieldValue)
									}
								/>
							</div>
						</div>

						<div className="mt-10 flex justify-center">
							<button
								type="submit"
								className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
							>
								Book for {priceChange == 0 ? props.address.price : priceChange}$
							</button>
						</div>
					</Form>
				)}
			</Formik>
		</div>
	);
}
