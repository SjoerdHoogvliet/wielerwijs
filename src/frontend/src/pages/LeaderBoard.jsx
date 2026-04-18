import { useEffect, useState } from "react"
import TeamDisplay from "../components/TeamDisplay"

export default function LeaderBoard() {
    const [teams, setTeams] = useState([])

    useEffect(() => {
        fetch('http://localhost:8080/api/team', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
        })
            .then(res => res.json())
            .then(data => {
                // Sort the teams by likeSaldo (likes-dislikes) ascending
                data.sort((a, b) => (b.likes - b.dislikes) - (a.likes - a.dislikes))
                setTeams(data)
            })
    }, [])

    return (
        <div className="mx-32">
            <div className="flex flex-col w-full">
                <div>
                    <h2 className="text-4xl p-4">Leaderboard</h2>
                    <div className="px-4 w-full space-y-4">
                        {teams.map(team => <TeamDisplay team={team} likeTeam={() => likeTeam(team.id)} dislikeTeam={() => dislikeTeam(team.id)} />)}
                    </div>
                </div>
            </div>
        </div>
    )
}