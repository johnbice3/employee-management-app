import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)
  const [showEmployeeAddForm, showForm] = useState(false)
  const [firstName, updateFirstName] = useState("")
  const [lastName, updateLastName] = useState("")
  const [email, updateEmail] = useState("")
  const [hireDate, updateHireDate] = useState("")

  return (
    <>
      <section id="center">
        <div className="hero">
          <img src={heroImg} className="base" width="170" height="179" alt="" />
          <img src={reactLogo} className="framework" alt="React logo" />
          <img src={viteLogo} className="vite" alt="Vite logo" />
        </div>
        <div>
          <h1>Employee Management Program</h1>
          <p>
            Manage your employees and compensation data.
          </p>
          <button
            type="button"
            className="primaryButton"
            onClick={() => showForm(true)}
          >
            Add employee
          </button>  
          {showEmployeeAddForm && (
            <form>
              <p> Add Employee Form </p>
              <label htmlFor="firstName">First Name </label>
              <input type="text" value={firstName} onChange={(event) => updateFirstName(event.target.value)} id="firstName" name="firstName"/> <br />
              <label htmlFor="lastName">Last Name </label>
              <input type="text" value={lastName} onChange={(event) => updateLastName(event.target.value)} id="lastName" name="lastName"/> <br />
              <label htmlFor="email">Email Address </label>
              <input type="text" value={email} onChange={(event) => updateEmail(event.target.value)} id="email" name="email"/> <br />
              <label htmlFor="hireDate">Hire Date </label>
              <input type="date" value={hireDate} onChange={(event) => updateHireDate(event.target.value)} id="hireDate" name="hireDate"/> <br />
              <button
                type="submit"
                className="confirmButton"
              >
              Submit Employee  
              </button>  
            </form>  
          )}
        </div>
        <button
          type="button"
          className="counter"
          onClick={() => setCount((count) => count + 1)}
        >
          Count is {count}
        </button>
      </section>

      <div className="ticks"></div>

      <section id="next-steps">
        <div id="docs">
          <svg className="icon" role="presentation" aria-hidden="true">
            <use href="/icons.svg#documentation-icon"></use>
          </svg>
          <h2>Documentation</h2>
          <p>Your questions, answered</p>
          <ul>
            <li>
              <a href="https://vite.dev/" target="_blank">
                <img className="logo" src={viteLogo} alt="" />
                Explore Vite
              </a>
            </li>
            <li>
              <a href="https://react.dev/" target="_blank">
                <img className="button-icon" src={reactLogo} alt="" />
                Learn more
              </a>
            </li>
          </ul>
        </div>
        <div id="social">
          <svg className="icon" role="presentation" aria-hidden="true">
            <use href="/icons.svg#social-icon"></use>
          </svg>
          <h2>Connect with us</h2>
          <p>Join the Vite community</p>
          <ul>
            <li>
              <a href="https://github.com/vitejs/vite" target="_blank">
                <svg
                  className="button-icon"
                  role="presentation"
                  aria-hidden="true"
                >
                  <use href="/icons.svg#github-icon"></use>
                </svg>
                GitHub
              </a>
            </li>
            <li>
              <a href="https://chat.vite.dev/" target="_blank">
                <svg
                  className="button-icon"
                  role="presentation"
                  aria-hidden="true"
                >
                  <use href="/icons.svg#discord-icon"></use>
                </svg>
                Discord
              </a>
            </li>
            <li>
              <a href="https://x.com/vite_js" target="_blank">
                <svg
                  className="button-icon"
                  role="presentation"
                  aria-hidden="true"
                >
                  <use href="/icons.svg#x-icon"></use>
                </svg>
                X.com
              </a>
            </li>
            <li>
              <a href="https://bsky.app/profile/vite.dev" target="_blank">
                <svg
                  className="button-icon"
                  role="presentation"
                  aria-hidden="true"
                >
                  <use href="/icons.svg#bluesky-icon"></use>
                </svg>
                Bluesky
              </a>
            </li>
          </ul>
        </div>
      </section>

      <div className="ticks"></div>
      <section id="spacer"></section>
    </>
  )
}

export default App
