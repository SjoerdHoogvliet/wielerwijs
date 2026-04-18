import { Chart, registerables } from "chart.js"
import { useEffect, useState, useRef, useLayoutEffect } from "react"
import { useParams } from "react-router"
import { getCategoryName } from "../utils/RiderCategoryEnumUtil.js"

Chart.register(...registerables)

export default function RennerPage() {
    const [renner, setRenner] = useState()
    const [rennerVotes, setRennerVotes] = useState([])
    const [category, setCategory] = useState("")
    const [showPercentages, setShowPercentages] = useState(false)
    const [statistics, setStatistics] = useState([])
    const canvasRef = useRef(null)
    const voteBarChart = useRef(null)
    let rennerID = useParams().rennerID

    useEffect(() => {
        getRenner()
        getRennerCategoryVotes()
        getRennerVoteStatistics()
    }, [])

    useLayoutEffect(() => {
        if (canvasRef.current) {
            if (voteBarChart.current) {
                voteBarChart.current.destroy()
            }

            let data = {
                labels: ["Klassementsrenner", "Klassieke renner", "Sprinter", "Klimmer", "Knecht", "Tijdrijder", "Aanvaller"],
                datasets : [{
                    label: showPercentages ? "Stemmen (%)" : "Stemmen (aantal)",
                    data: showPercentages
                        ? [
                            statistics.percentageKlassementsRennerVotes ?? 0,
                            statistics.percentageKlassiekeRennerVotes ?? 0,
                            statistics.percentageSprinterVotes ?? 0,
                            statistics.percentageKlimmerVotes ?? 0,
                            statistics.percentageKnechtVotes ?? 0,
                            statistics.percentageTijdrijderVotes ?? 0,
                            statistics.percentageAanvallerVotes ?? 0
                        ]
                        : [
                            statistics.klassementsRennerVotes ?? 0,
                            statistics.klassiekeRennerVotes ?? 0,
                            statistics.sprinterVotes ?? 0,
                            statistics.klimmerVotes ?? 0,
                            statistics.knechtVotes ?? 0,
                            statistics.tijdrijderVotes ?? 0,
                            statistics.aanvallerVotes ?? 0
                        ],
                }]
            }

            voteBarChart.current = new Chart(canvasRef.current, {
                type: 'bar',
                data: data,
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });

            return () => {
                voteBarChart.current.destroy()
            }
        }
    }, [voteBarChart, canvasRef, statistics, showPercentages])

    function getRenner() {
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
    }

    function getRennerCategoryVotes() {
        fetch(`http://localhost:8080/api/categoryvote/renner/${rennerID}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
        })
            .then(res => res.json())
            .then(data => {
                console.log(data)
                setRennerVotes(data)
            })
    }

    function vote() {
        fetch(`http://localhost:8080/api/categoryvote`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            },
            body: JSON.stringify({ category: category, userId: sessionStorage.getItem("userId"), rennerId: rennerID })
        })
        getRenner()
        getRennerCategoryVotes()
        getRennerVoteStatistics()
    }

    function removeVote() {
        fetch(`http://localhost:8080/api/categoryvote`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            },
            body: JSON.stringify({ userId: sessionStorage.getItem("userId"), rennerId: rennerID })
        })

        getRenner()
        getRennerCategoryVotes()
        getRennerVoteStatistics()
    }

    function getRennerVoteStatistics() {
        fetch(`http://localhost:8080/api/categoryvote/renner/${rennerID}/statistics`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
        })
            .then(res => res.json())
            .then(data => {
                console.log(data)
                setStatistics(data)
            })
    }

    return (
        <>
            <div className="p-8">

                <h1 className="text-4xl font-bold py-4">{renner && renner.naam}</h1>

                <h2 className="text-2xl py-4">Stem!</h2>
                <div className="flex py-2">
                    {/* Dropdown menu */}
                    <select onChange={e => setCategory(e.target.value)} className="p-2 rounded-md duration-150 mr-4">
                        <option value="KLASSEMENTSRENNER">Klassementsrenner</option>
                        <option value="KLASSIEKE_RENNER">Klassieke renner</option>
                        <option value="SPRINTER">Sprinter</option>
                        <option value="KLIMMER">Klimmer</option>
                        <option value="KNECHT">Knecht</option>
                        <option value="TIJDRIJDER">Tijdrijder</option>
                        <option value="AANVALLER">Aanvaller</option>
                    </select>
                    <button 
                        onClick={() => vote()}
                        className="bg-secondary hover:bg-secondary-hovered duration-150 max-h-12 text-white px-4 py-2 rounded-md hover:cursor-pointer"
                    >
                        Stem
                    </button>
                </div>

                {rennerVotes.find(vote => vote.userId == sessionStorage.getItem("userId")) && 
                    <div className="flex py-2">
                        <p>
                            Huidige stem: <span className="font-bold">{getCategoryName(rennerVotes.find(vote => vote.userId == sessionStorage.getItem("userId")).category)}</span>
                            <span 
                                onClick={() => removeVote()}
                                className="text-secondary hover:font-bold duration-150 hover:cursor-pointer mx-2"
                            >
                                verwijder
                            </span>
                            om een nieuwe stem uit te kunnen brengen
                        </p>
                    </div>
                }

                <div>
                    <h2 className="text-2xl py-4">Statistieken</h2>
                    <p value={showPercentages} onClick={() => setShowPercentages(!showPercentages)} className="flex items-center hover:font-bold hover:cursor-pointer">{showPercentages ? "Show vote numbers" : "Show percentages"}</p>
                    <div className="flex space-x-4 max-w-full">
                        <canvas ref={canvasRef}></canvas>
                    </div>
                </div>
            </div>
        </>
    )
}