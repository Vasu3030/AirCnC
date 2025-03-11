const url = "http://localhost:8090/comment";

const getAllCommentByAddress = async (id, callback) => {
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

const deleteComment = async (token, id, callback) => {
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

const postComment = async (token, addressId, content, callback) => {
    try {
        const formData = new FormData();
        formData.append("content", content);
        formData.append("id_address", addressId);
        const response = await fetch(url, {
            method: "POST",
            mode: "cors",
            headers: {
                Authorization: `Bearer ${token}`,
            },
            body: formData,
        });
        const json = await response.json();
        return callback(null, json, response.status);
    } catch (error) {
        console.error(error);
    }
};

export { getAllCommentByAddress, deleteComment, postComment };
