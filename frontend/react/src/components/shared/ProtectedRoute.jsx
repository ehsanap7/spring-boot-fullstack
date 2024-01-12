import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../context/AuthContext.jsx";

const ProtectedRoute = ({children}) => {

    const navigate = useNavigate();
    const {isUserAuthenticated} = useAuth();

    useEffect(() => {
        !isUserAuthenticated() && navigate("/");
    });

    return isUserAuthenticated() ? children : "";

}

export default ProtectedRoute;