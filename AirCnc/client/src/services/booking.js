const url = "http://localhost:8090/booking";

const bookAddress = async (token, fromDate, toDate, addressId, price, callback) => {
    try {
        const formData = new FormData();
        formData.append("from", fromDate);
        formData.append("to", toDate);
        formData.append("id_address", addressId);
        formData.append("price", price);

        const response = await fetch(url, {
            method: "POST",
            mode: "cors",
            headers: {
                Authorization: `Bearer ${token}`,
            },
            credentials: "include",
            body: formData,
        });

        const json = await response.json();
        return callback(null, json, response.status);
    } catch (error) {
        console.error(error);
    }
};

const getBookingByUserId = async (token, id, callback) => {
    try {
        const response = await fetch(url + "/user/" + id, {
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

const getBookingByUserIdAndStatus = async (token, id, status, callback) => {
    try {
        const response = await fetch(url + "/user/status/" + id + "?status=" + status, {
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

const getBookingByUserIdIncoming = async (token, id, callback) => {
    try {
        const response = await fetch(url + "/user/incoming/" + id, {
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

const deleteBooking = async (token, id, callback) => {
    try {
        const response = await fetch(url + "/" + id, {
            method: "DELETE",
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

const updateBooking = async (token, id, status, callback) => {
    try {
        const formData = new FormData();
        formData.append("status", status);
        const response = await fetch(url + "/" + id, {
            method: "PUT",
            mode: "cors",
            headers: {
                Authorization: `Bearer ${token}`,
            },
            body: formData,
            credentials: "include"
        });

        const json = await response.json();
        return callback(null, json, response.status);
    } catch (error) {
        console.error(error);
    }
};

export { bookAddress, getBookingByUserId, deleteBooking, getBookingByUserIdAndStatus, getBookingByUserIdIncoming, updateBooking };