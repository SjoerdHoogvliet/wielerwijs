import { useEffect, useState } from "react";
import TeamDisplay from "../components/TeamDisplay";
import { useNavigate } from "react-router";

export default function DisplayTeams() {
    const [teams, setTeams] = useState([])
    const navigate = useNavigate();

     async function loadData () {
        console.log("Bearer " + sessionStorage.getItem("jwtToken"))

        await fetch('http://localhost:8080/api/team', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
        })
            .then(res => res.json())
            .then(data => {
                setTeams(data)
            })
    }

    useEffect(() => {
        loadData()
    }, [])

    return (
        <div className="mx-32">
            <div className="flex w-full">
                <div>
                    <h2 className="text-4xl p-4">Teams beoordelen</h2>
                    <p onClick={() => navigate("/leaderboard")} className="ml-4 mb-4 text-secondary hover:cursor-pointer hover:font-bold duration-150">Ga naar de ranglijst</p>
                    <div className="grid-cols-4 grid space-y-8 space-x-8 px-4">
                        {teams.map(team => <TeamDisplay team={team} square allowRemoval={sessionStorage.getItem("userRole") == "ROLE_ADMIN"} />)}
                    </div>
                </div>
            </div>
        </div>
    )
}