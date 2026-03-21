import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { RouterProvider, createBrowserRouter } from 'react-router'
import './index.css'
import CreateTeam from './pages/createTeam.jsx'
import DisplayTeams from './pages/DisplayTeams.jsx'
import NavBar from './components/NavBar.jsx'

let router = createBrowserRouter([
    {
      path: '/',
      element: <CreateTeam />,
    },
    {
      path: '/teams',
      element: <DisplayTeams />
    }
  ]
)

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <NavBar />
    <RouterProvider router={router} />
  </StrictMode>,
)
