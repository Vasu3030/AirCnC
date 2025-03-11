import { useState } from "react";
import { register, me, login } from "../services/auth";
import { useNavigate, Link } from "react-router-dom";

export default function Register(props) {
	const [username, setUsername] = useState();
	const [password, setPassword] = useState();
	const navigate = useNavigate();
	function handleSubmit(event) {
		event.preventDefault();
		register(username, password, (error, res, status) => {
			if (error) {
				alert("error:", error.message);
			} else if (status === 201) {
				login(username, password, async (error, res, status) => {
					if (error) {
						alert(error.message);
					} else if (status == 200) {
						localStorage.setItem("token", res.token);
						me(res.token, (err, res, status) => {
							if (status == 200) {
								props.handleLogin(res.id, res.role, res.username)
								navigate("/")
							} else {
								alert("error")
							}
						})
					} else {
						alert(res.message);
					}
				});
			} else {
				alert(res.message);
			}
		});
	}

	function handleUsernameChange(event) {
		setUsername(event.target.value);
	}

	function handlePasswordChange(event) {
		setPassword(event.target.value);
	}

	return (
		<div className="flex justify-center items-center h-screen">
			<div className="w-full max-w-sm p-4 bg-white border border-gray-200 rounded-lg shadow sm:p-6 md:p-8 dark:bg-gray-800 dark:border-gray-700">
				<form className="space-y-6" onSubmit={handleSubmit}>
					<h5 className="text-xl font-medium text-gray-900 dark:text-white">
						Create an account
					</h5>
					<div>
						<label className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
							Your username
						</label>
						<input
							type="text"
							className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
							placeholder="username"
							onChange={handleUsernameChange}
							required
						/>
					</div>
					<div>
						<label className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
							Your password
						</label>
						<input
							type="password"
							name="password"
							id="password"
							placeholder="••••••••"
							className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
							onChange={handlePasswordChange}
							required
						/>
					</div>

					<button
						type="submit"
						className="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
					>
						Create your account
					</button>
					<div className="text-sm font-medium text-gray-500 dark:text-gray-300">
						Already registered?{" "}
						<Link
							to="/login"
							className="text-blue-700 hover:underline dark:text-blue-500"
						>
							Sign in
						</Link>
					</div>
				</form>
			</div>
		</div>
	);
}
