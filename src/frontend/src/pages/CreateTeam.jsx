import { useEffect, useState } from "react"
import RennerDisplay from "../components/RennerDisplay.jsx"

export default function CreateTeam() {
  const [renners, setRenners] = useState([])
  const [team, setTeam] = useState([])
  const [teamName, setTeamName] = useState("")
  const [rennerSearch, setRennerSearch] = useState("")

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

  function filterRenners() {
    if (rennerSearch === "") {
      return renners
    }
    return renners.filter(renner => renner.naam.toLowerCase().includes(rennerSearch.toLowerCase()))
  }

  return (
    <div className="mx-32 p-2 flex flex-col">
      <input 
        type="text" 
        placeholder="Nieuwe teamnaam"  
        value={teamName} 
        onChange={e => setTeamName(e.target.value)}
        className="p-2 rounded-md duration-150"
      />
      <div className="flex space-x-8">
        <div className="h-[80vh] w-xl">
          <h2 className="text-3xl py-4">Beschikbare renners</h2>
          <input 
            type="text" 
            placeholder="Zoek een renner"  
            value={rennerSearch} 
            onChange={e => setRennerSearch(e.target.value)}
            className="p-2 m-2 rounded-md duration-150"
          />
          <div className="h-[65vh] flex flex-col space-x-4 space-y-4 overflow-y-auto">
            {filterRenners().map(renner => <RennerDisplay renner={renner} function={() => addRennerToTeam(renner)} />)}
          </div>
        </div>
        <div className="w-xl h-[80vh]">
          <h2 className="text-3xl py-4">Team</h2>
          <div className="flex flex-col space-x-4 space-y-4 h-[70vh] overflow-y-auto">
            {team.map(renner => <RennerDisplay renner={renner} function={() => removeRennerFromTeam(renner)} removal />)}
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

