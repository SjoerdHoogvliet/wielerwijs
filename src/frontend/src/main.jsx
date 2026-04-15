import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { RouterProvider, createBrowserRouter } from 'react-router'
import './index.css'
import CreateTeam from './pages/createTeam.jsx'
import DisplayTeams from './pages/DisplayTeams.jsx'
import RennerPage from './pages/RennerPage.jsx'
import LoginPage from './pages/LoginPage.jsx'
import ProfilePage from './pages/ProfilePage.jsx'
import LeaderBoard from './pages/LeaderBoard.jsx'
import NavBar from './components/NavBar.jsx'

let router = createBrowserRouter([
    {
      path: '/',
      element: <CreateTeam />,
    },
    {
      path: '/teams',
      element: <DisplayTeams />
    },
    {
      path: '/renner/:rennerID',
      element: <RennerPage />
    },
    {
      path: '/login',
      element: <LoginPage />
    },
    {
      path: '/profile',
      element: <ProfilePage />
    },
    {
      path: '/leaderboard',
      element: <LeaderBoard />
    }
  ]
)

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <NavBar />
    <RouterProvider router={router} />
  </StrictMode>,
)
