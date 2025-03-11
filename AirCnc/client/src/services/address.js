const url = "http://localhost:8090/address";

const getAllAddress = async (token, callback) => {
	try {
		if (token == null)
			token = ""
		const response = await fetch(url, {
			method: "GET",
			mode: "cors",
			headers: {
				Authorization: `Bearer ${token}`,
			},
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};

const getAddressById = async (id, callback) => {
	try {
		const response = await fetch(url + "/" + id, {
			method: "GET",
			mode: "cors",
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};

const getAddressByUserId = async (id, callback) => {
	try {
		const response = await fetch(url + "/user/" + id, {
			method: "GET",
			mode: "cors",
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};

const updateAddress = async (token, id, data, callback) => {
	try {
		const response = await fetch(url + "/" + id, {
			method: "PUT",
			mode: "cors",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${token}`,
			},
			body: JSON.stringify(data),
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};

const createAddress = async (token, data, callback) => {
	try {
		const response = await fetch(url, {
			method: "POST",
			mode: "cors",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${token}`,
			},
			body: JSON.stringify(data),
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};

const deletePicture = async (token, id, callback) => {
	try {
		const response = await fetch(url + "/picture/" + id, {
			method: "DELETE",
			mode: "cors",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${token}`,
			}
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};

const addPicture = async (token, id, data, callback) => {
	try {
		const response = await fetch(url + "/picture/" + id, {
			method: "POST",
			mode: "cors",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${token}`
			},
			body: JSON.stringify({
				url: data
			}),
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};


const deleteAddress = async (token, id, callback) => {
	try {
		const response = await fetch(url + "/" + id, {
			method: "DELETE",
			mode: "cors",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${token}`,
			}
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};

export { getAllAddress, getAddressById, getAddressByUserId, updateAddress, createAddress, deletePicture, addPicture, deleteAddress };
