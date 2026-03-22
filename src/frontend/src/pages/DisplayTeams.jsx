import { useEffect, useState } from "react";
import TeamDisplay from "../components/TeamDisplay";

export default function DisplayTeams() {
    const [teams, setTeams] = useState([])

     async function loadData () {
        await fetch('http://localhost:8080/api/team')
            .then(res => res.json())
            .then(data => {
                setTeams(data)
            })
    }

    useEffect(() => {
        loadData()
    }, [])

    function likeTeam(id) {
        fetch('http://localhost:8080/api/team/' + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ like: true })
        })

        loadData()
    }

    function dislikeTeam(id) {
        fetch('http://localhost:8080/api/team/' + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ like: false })
        })

        loadData()
    }

    function removeTeam(id) {
        fetch('http://localhost:8080/api/team/' + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })

        loadData()
    }

    return (
        <div className="mx-32">
            <div className="flex w-full">
                <div>
                    <h2 className="text-4xl p-4">Teams</h2>
                    <div className="grid-cols-4 grid space-y-8 space-x-8 px-4">
                        {teams.map(team => <TeamDisplay team={team} likeTeam={() => likeTeam(team.id)} dislikeTeam={() => dislikeTeam(team.id)} removeTeam={() => removeTeam(team.id)} />)}
                    </div>
                </div>
            </div>
        </div>
    )
}