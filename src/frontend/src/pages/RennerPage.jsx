import { useEffect, useState } from "react"
import { useParams } from "react-router"

export default function RennerPage() {
    const [renner, setRenner] = useState()
    let rennerID = useParams().rennerID

    useEffect(() => {
        fetch(`http://localhost:8080/api/renner/${rennerID}`, {
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
        })
            .then(res => res.json())
            .then(data => {
                console.log(data)
                setRenner(data)
            })
    }, [])

    return (
        <>
            <div className="p-8">

                <h1 className="text-4xl font-bold">{renner && renner.naam}</h1>
            </div>
        </>
    )
}