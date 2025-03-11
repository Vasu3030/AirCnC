import { getAllUser } from "../services/user";
import { useEffect, useState } from "react";
import UserCard from "../components/userCard";

export default function Admin() {
    const [users, setUsers] = useState();

    useEffect(() => {
        getAllUser((err, res, status) => {
            if (status === 200) {
                setUsers(res);
            } else {
                console.log(status, err);
            }
        });
    }, []);

    if (users) {
        return (
            <div>
                <p className="tracking-tighter text-gray-500 md:text-lg dark:text-gray-400 ml-3">Number of results : {users.length}</p>
                {users.map((user) => (
                    <div key={user.id}>{user.role != "ROLE_ADMIN" ? <UserCard user={user} /> : null}</div>
                ))}
            </div>
        );
    } else {
        <h1>No users available</h1>;
    }
}
