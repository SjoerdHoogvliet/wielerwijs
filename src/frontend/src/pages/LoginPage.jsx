import { useState } from "react"

export default function LoginPage() {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [loginSuccess, setLoginSuccess] = useState(false)

    function attemptLogin() {
        console.log("Attempting login")
        fetch('http://localhost:8080/api/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: username, password: password })
        }).then(res => res.json())
            .then(data => {
                console.log(data)
                setLoginSuccess(data != null)
                if (data != null) {
                    sessionStorage.setItem("jwtToken", data.token)
                }
            })
    }

    return (
        <div className="flex flex-col p-8 space-y-4">
            <h2 className="text-4xl font-bold">Login</h2>
            <input type="text" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} className="p-2 rounded-md duration-150" />
            <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} className="p-2 rounded-md duration-150" />
            <button 
                className="p-4 mt-auto bg-secondary hover:bg-secondary-hovered w-fit duration-150 max-h-12 text-white px-4 py-2 rounded-md hover:cursor-pointer"
                onClick={() => attemptLogin()}
            >
                Login
            </button>
            {loginSuccess && <h1 className="text-4xl p-4 font-bold text-green">Succes!</h1>}
        </div>
    )
}