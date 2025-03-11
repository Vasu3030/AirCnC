const url = "http://localhost:8090/user";

const getAllUser = async (callback) => {
    try {
        const response = await fetch(url, {
            method: "GET",
            mode: "cors",
        });
        const json = await response.json();
        return callback(null, json, response.status);
    } catch (error) {
        console.error(error);
    }
};

const getUserById = async (userId, callback) => {
    try {
        const response = await fetch(url + "/" + userId, {
            method: "GET",
            mode: "cors",
        });
        const json = await response.json();
        return callback(null, json, response.status);
    } catch (error) {
        console.error(error);
    }
};

const deleteUser = async (token, id, callback) => {
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

export { getUserById, deleteUser, getAllUser };
