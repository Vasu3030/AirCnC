import React from "react";
import { useNavigate } from "react-router-dom";
import { Formik, Field, Form } from "formik";
import {
	updateAddress,
	getAddressById,
	createAddress,
} from "../services/address";

export default function AddressForm(props) {
	const navigate = useNavigate();
	const initialValues = {
		name: "",
		street: "",
		city: "",
		country: "",
		postalCode: "",
		price: "",
	};

	const onSubmit = (values) => {
		const token = localStorage.getItem("token");
		if (props.action == "edit") {
			updateAddress(
				token,
				props.address.id,
				values,
				async (error, res, status) => {
					if (error) {
						alert(error.message);
					} else if (status == 200) {
						getAddressById(props.address.id, (err, res, status) => {
							if (status === 200) {
								props.setAddresse(res);
							} else {
								console.log(status, err);
							}
						});
					} else {
						alert(res.message);
					}
				}
			);
		} else {
			createAddress(token, values, async (error, res, status) => {
				if (error) {
					alert(error.message);
				} else if (status == 201) {
					navigate(-1);
				} else {
					alert(res.message);
				}
			});
		}
	};

	return (
		<div className="p-5 flex flex-row">
			<Formik initialValues={initialValues} onSubmit={onSubmit}>
				{({ values, setFieldValue }) => (
					<Form>
						<div>
							<label
								className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
								htmlFor="name"
							>
								Name
							</label>
							<Field
								as="textarea"
								className="h-20 bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
								id="name"
								name="name"
							/>
						</div>
						<div className="mt-3">
							<label
								className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
								htmlFor="country"
							>
								Country
							</label>
							<Field
								type="text"
								className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
								id="country"
								name="country"
							/>
						</div>
						<div className="mt-3">
							<label
								className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
								htmlFor="city"
							>
								City
							</label>
							<Field
								type="text"
								className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
								id="city"
								name="city"
							/>
						</div>
						<div className="mt-3">
							<label
								className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
								htmlFor="street"
							>
								Street
							</label>
							<Field
								type="text"
								className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
								id="street"
								name="street"
							/>
						</div>
						<div className="mt-3">
							<label
								className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
								htmlFor="postalCode"
							>
								Postal Code
							</label>
							<Field
								type="text"
								className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
								id="postalCode"
								name="postalCode"
							/>
						</div>
						<div className="mt-3">
							<label
								className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
								htmlFor="price"
							>
								Price per night
							</label>
							<Field
								type="text"
								className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
								id="price"
								name="price"
							/>
						</div>
						<div className="flex justify-center mt-8">
							<button
								type="submit"
								className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
							>
								{props.action == "edit" ? "Update" : "Create"}
							</button>
						</div>
					</Form>
				)}
			</Formik>
		</div>
	);
}
