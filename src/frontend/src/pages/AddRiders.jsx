import { useEffect, useState } from "react"

export default function AddRiders() {
    const [isSuccess, setIsSuccess] = useState(false)
    const [riderName, setRiderName] = useState("")

    function addRider(riderName) {
        fetch('http://localhost:8080/api/renner', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            },
            body: JSON.stringify({ naam: riderName })
        })
            .then(res => setIsSuccess(res.ok))
    }

    return (
        <div className="mx-32 p-2 flex">
            <div className="h-[80vh] w-xl flex-col">
                <h2 className="text-3xl py-4">Nieuwe renner toevoegen</h2>
                <input 
                    type="text" 
                    placeholder="Voer de naam in"  
                    value={riderName} 
                    onChange={e => setRiderName(e.target.value)}
                    className="p-2 m-2 rounded-md duration-150"
                />
                <div className="flex pt-2">
                    <button
                        className="p-2 rounded-md duration-150 bg-secondary hover:bg-secondary-hovered text-white"
                        onClick={() => addRider(riderName)}
                    >
                        Toevoegen
                    </button>
                </div>
                {isSuccess && <h1 className="text-4xl p-4 font-bold text-green-500">Succes!</h1>}
            </div>
        </div>
    )
}