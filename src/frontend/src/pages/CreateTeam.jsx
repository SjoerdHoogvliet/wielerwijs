import { useEffect, useState } from "react"
import RennerDisplay from "../components/RennerDisplay.jsx"

export default function CreateTeam() {
  const [renners, setRenners] = useState(null)
  const [rennerElements, setRennerElements] = useState([])
  const [team, setTeam] = useState([])
  const [teamName, setTeamName] = useState("")

  useEffect(() => {
    fetch('http://localhost:8080/api/renner')
      .then(res => res.json())
      .then(data => {
        console.log(data)
        setRennerElements([])
        setRenners(data)
        if(data.length === 0) {
          setRennerElements(<div>Geen renners gevonden</div>)
          return
        }
        data.map(renner => {
          setRennerElements(rennerElements => [...rennerElements, <RennerDisplay renner={renner} function={() => rennerSwitch(renner)} />])
        })
      })
  }, [])

  function rennerSwitch(renner) {
    console.log(renner)
    if (team.includes(renner)) {
      setTeam(team.filter(r => r.id !== renner.id))
      return
    }
    setTeam(team => [...team, renner])

    console.log(team)
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

    console.log("Team: ", teamObject)
  }

  return (
    <div className="mx-32 p-4">
      <input 
        type="text" 
        placeholder="Nieuwe teamnaam"  
        value={teamName} 
        onChange={e => setTeamName(e.target.value)}
      />
      <div className="flex space-x-8">
        <div>
          <h2>Renners beschikbaar</h2>
          <div className="grid grid-cols-3 w-lg">
            {rennerElements}   
          </div>
        </div>
        <div>
          <h2>Team</h2>
          <div className="grid grid-cols-3 w-lg">
            {team.map(renner => <RennerDisplay renner={renner} function={() => rennerSwitch(renner)} />)}
          </div>
        </div>
        <button 
          className="bottom-0 right-0 bg-secondary hover:bg-primary hover:text-background duration-150 max-h-12 text-white px-4 py-2 rounded-md hover:cursor-pointer"
          onClick={() => saveTeam()}
        >
          Save
        </button>
      </div>
    </div>
  )
}

