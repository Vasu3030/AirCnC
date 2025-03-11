const url = "http://localhost:8090/";

const login = async (username, password, callback) => {
	try {
		const response = await fetch(url + "authenticate", {
			method: "POST",
			mode: "cors",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({
				username: username,
				password: password,
			}),
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};

const register = async (username, password, callback) => {
	try {
		const response = await fetch(url + "register", {
			method: "POST",
			mode: "cors",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({
				username: username,
				password: password,
			}),
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};

const me = async (token, callback) => {
	try {
		const response = await fetch(url + "me", {
			method: "GET",
			mode: "cors",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${token}`,
			},
			credentials: "include",
		});
		const json = await response.json();
		return callback(null, json, response.status);
	} catch (error) {
		console.error(error);
	}
};


export { login, register, me };
