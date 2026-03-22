import { useEffect, useState } from "react"
import RennerDisplay from "../components/RennerDisplay.jsx"

export default function CreateTeam() {
  const [renners, setRenners] = useState([])
  const [rennerElements, setRennerElements] = useState([])
  const [team, setTeam] = useState([])
  const [teamName, setTeamName] = useState("")

  useEffect(() => {
    fetch('http://localhost:8080/api/renner')
      .then(res => res.json())
      .then(data => {
        setRenners(data)
      })
  }, [])

  function addRennerToTeam(renner) {
    console.log(renner)
    console.log(team)
    
    if (team.includes(renner)) {
      console.log("Al gebruikt")
      return
    }

    setTeam(team => [...team, renner])

    console.log(team)
  }

  function removeRennerFromTeam(renner) {
    setTeam(team => team.filter(r => r.id !== renner.id))
  }

  function saveTeam() {
    var teamObject = {
      naam: teamName,
      renners: team.map(renner => renner.id)
    }

    fetch('http://localhost:8080/api/team', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(teamObject)
    })

    setTeam([])
    setTeamName("")

    console.log("Team: ", teamObject)
  }

  return (
    <div className="mx-32 p-4 flex flex-col">
      <input 
        type="text" 
        placeholder="Nieuwe teamnaam"  
        value={teamName} 
        onChange={e => setTeamName(e.target.value)}
      />
      <div className="flex space-x-8">
        <div>
          <h2 className="text-3xl py-4">Beschikbare renners</h2>
          <div className="grid grid-cols-3 w-lg">
            {renners.map(renner => <RennerDisplay renner={renner} function={() => addRennerToTeam(renner)} />)}
          </div>
        </div>
        <div>
          <h2 className="text-3xl py-4">Team</h2>
          <div className="grid grid-cols-3 w-lg">
            {team.map(renner => <RennerDisplay renner={renner} function={() => removeRennerFromTeam(renner)} />)}
          </div>
        </div>
        <button 
          className="justify-end mt-auto bg-secondary hover:bg-secondary-hovered duration-150 max-h-12 text-white px-4 py-2 rounded-md hover:cursor-pointer"
          onClick={() => saveTeam()}
        >
          Opslaan
        </button>
      </div>
    </div>
  )
}

