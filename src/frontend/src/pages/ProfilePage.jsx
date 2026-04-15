import { Chart, registerables } from "chart.js"
import { useEffect, useState, useRef, useLayoutEffect } from "react"
import { getCategoryName } from "../utils/RiderCategoryEnumUtil.js"

Chart.register(...registerables)

export default function ProfilePage() {
    const [user, setUser] = useState()
    const [statistics, setStatistics] = useState([])
    const canvasRef = useRef(null)
    const voteBarChart = useRef(null)
    const [showPercentages, setShowPercentages] = useState(false)

    useEffect(() => {
        fetch(`http://localhost:8080/api/user/${sessionStorage.getItem("userId")}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
        })
            .then(res => res.json())
            .then(data => {
                console.log(data)
                setUser(data)
            })

        getUserVoteStatistics()
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

    function getUserVoteStatistics() {
        fetch(`http://localhost:8080/api/categoryvote/user/${sessionStorage.getItem("userId")}/statistics`, {
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

    function getMostLikedCategory() {
        let categoriesConsidered = {
            "KLASSEMENTSRENNER" : statistics.klassementsRennerVotes, 
            "KLASSIEKE_RENNER" : statistics.klassiekeRennerVotes,
            "SPRINTER" : statistics.sprinterVotes,
            "KLIMMER" : statistics.klimmerVotes,
            "KNECHT" : statistics.knechtVotes,
            "TIJDRIJDER" : statistics.tijdrijderVotes,
            "AANVALLER" : statistics.aanvallerVotes
        }
        var items = Object.keys(categoriesConsidered).map(function(key) {
            return [key, categoriesConsidered[key]];
        });

        items.sort(function(first, second) {
            // if value of second - first is positive second gets placed to the front (as its bigger than the first item)
            return second[1] - first[1];
        });

        var mostLikedCategory = items[0].splice(",")[0]
        return getCategoryName(mostLikedCategory)
    }

    return (
        <div className="p-8">
            <h1 className="text-4xl font-bold py-4">{user && user.username}</h1>
            <div className="flex flex-col py-2">
                <div className="flex-col space-x-4 py-4 max-w-full">
                    <p>
                        Totaal aantal uitgebrachte stemmen: {statistics.totalVotes}
                    </p>
                    <p>
                        Favoriete categorie: {(getMostLikedCategory())}
                    </p>
                </div>
                <p value={showPercentages} onClick={() => setShowPercentages(!showPercentages)} className="flex items-center hover:font-bold hover:cursor-pointer">{showPercentages ? "Show vote numbers" : "Show percentages"}</p>
                <div className="flex space-x-4 max-w-full">
                    <canvas ref={canvasRef}></canvas>
                </div>
            </div>
        </div>
    )
}