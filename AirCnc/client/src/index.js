import React, { useEffect, useState } from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import Home from "./pages/home";
import Login from "./pages/login";
import Profil from "./pages/profil";
import Register from "./pages/register";
import Booking from "./pages/booking";
import Address from "./pages/address";
import Admin from "./pages/admin";
import AddressManaging from "./pages/addressManaging";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LoggedNavBar from "./components/loggedNavBar";
import NotLoggedNavBar from "./components/notLoggedNavBar";
import AddressCreate from "./pages/addressCreate";

import { me } from "./services/auth";
import UserProfil from "./pages/userProfil";

const App = () => {

	const [isAuthenticated, setIsAuthenticated] = useState(null);
	const [role, setRole] = useState(null);

	const handleLogin = async (id, role, username) => {
		localStorage.setItem("id", id);
		localStorage.setItem("role", role);
		localStorage.setItem("username", username);

		setIsAuthenticated(true);
		setRole(role)
	};

	const handleLogout = () => {
		localStorage.removeItem("token");
		localStorage.removeItem("id");
		localStorage.removeItem("role");
		localStorage.removeItem("username");
		setIsAuthenticated(false);
		setRole()
	};

	useEffect(() => {
		const token = localStorage.getItem("token");
		if (token == null) {
			setIsAuthenticated(false);
			setRole()
		}
		me(token, (err, res, status) => {
			if (status == 200) {
				localStorage.setItem("role", res.role);
				localStorage.setItem("username", res.username);
				localStorage.setItem("id", res.id);
				setIsAuthenticated(true)
				setRole(res.role)
			} else {
				localStorage.removeItem("token");
				localStorage.removeItem("id");
				localStorage.removeItem("role");
				localStorage.removeItem("username");
				setIsAuthenticated(false)
				setRole()
			}
		})

	}, [])

	return (
		<React.StrictMode>
			<Router>
				{isAuthenticated ? <LoggedNavBar handleLogout={handleLogout} role={role} /> : <NotLoggedNavBar />}
				<Routes>
					<Route path="" element={<Home />} />
					<Route path="register" element={isAuthenticated ? <Home /> : <Register handleLogin={handleLogin} />} />
					<Route path="login" element={isAuthenticated ? <Home /> : <Login handleLogin={handleLogin} />} />
					<Route path="profil" element={isAuthenticated ? <Profil /> : <Login handleLogin={handleLogin} />} />
					<Route path="address/managing" element={isAuthenticated ? <AddressManaging /> : <Login handleLogin={handleLogin} />} />
					<Route path="address/create" element={isAuthenticated ? <AddressCreate /> : <Login handleLogin={handleLogin} />} />
					<Route path="admin" element={isAuthenticated && role == "ROLE_ADMIN" ? <Admin /> : <Home />} />
					<Route path="address/:id" element={<Address isAuthenticated={isAuthenticated} />} />
					<Route path="user/:id" element={<UserProfil />} />
					<Route path="booking" element={isAuthenticated ? <Booking /> : <Login handleLogin={handleLogin} />} />
				</Routes>
			</Router>
		</React.StrictMode>
	);
};

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);
