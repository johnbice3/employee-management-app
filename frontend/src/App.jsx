import { useState, useEffect } from 'react'
import './App.css'

function App() {
  const [firstName, updateFirstName] = useState("")
  const [lastName, updateLastName] = useState("")
  const [email, updateEmail] = useState("")
  const [hireDate, updateHireDate] = useState("")
  const [pendingEERecord, addNewEmployee] = useState(null)
  const [employeeSubmissionError, updateSubmissionError] = useState(null)
  const [employeeList, displayEmployees] = useState(null)
  const [currentView, changeCurrentView] = useState("home")

  function handleSubmit(event){
    event.preventDefault()

    const employeeData = {
      firstName: firstName,
      lastName: lastName,
      email: email,
      hireDate: hireDate
    }
    addNewEmployee(null)
    updateSubmissionError(null)
    fetch("http://localhost:8080/employees", {
      method: "POST", 
      headers: {
        "Content-Type":"application/json"
      },
      body: JSON.stringify(employeeData)
    })
    .then((response) => {
      return response.json()
    })
    .then((data) => {
      addNewEmployee(data)
      updateFirstName("")
      updateLastName("")    
      updateEmail("")
      updateHireDate("")
    })
    .catch((error) => {
      console.error(error)
      updateSubmissionError(error)
    })
  }
  useEffect(() => {
    fetch("http://localhost:8080/employees")
    .then((response) => {
      return response.json()
    })
    .then((data) => {
      displayEmployees(data)
    })
    }, [])
  return (
    <>
    <header>
      <h1> Employee Management </h1>
      <nav> 
        <button
          type="button"
          className="navigationButton"
          onClick={() => changeCurrentView("home")}
        >
        Home 
        </button>  
        <button
          type="button"
          className="navigationButton"
          onClick={() => changeCurrentView("addEmployees")}
        >
          Add employees
        </button> 
        <button
          type="button"
          className="navigationButton"
          onClick={() => changeCurrentView("employeeListView")}
        >
        View Employees
        </button>
      </nav>
    </header>
    <main>
      {currentView === "home" &&
      <section className="homeView">
        <div>
          <h2>Employee Management Program</h2>
          <p>
            Manage your employees and compensation data.
          </p>
        </div>
      </section>
      }
      {currentView === "addEmployees" &&
      <div>
        <form 
        onSubmit={handleSubmit}
        className="employeeForm"
        >
          <h2> Employee Submission Form </h2>
          <div className="formField">
          <label htmlFor="firstName">First Name </label>
          <input type="text" value={firstName} onChange={(event) => updateFirstName(event.target.value)} id="firstName" name="firstName"/>
          </div>
          <div className="formField">
          <label htmlFor="lastName">Last Name </label>
          <input type="text" value={lastName} onChange={(event) => updateLastName(event.target.value)} id="lastName" name="lastName"/>
          </div>
          <div className="formField">
          <label htmlFor="email">Email Address </label>
          <input type="text" value={email} onChange={(event) => updateEmail(event.target.value)} id="email" name="email"/>
          </div>
          <div className="formField">
          <label htmlFor="hireDate">Hire Date </label>
          <input type="date" value={hireDate} onChange={(event) => updateHireDate(event.target.value)} id="hireDate" name="hireDate"/>
          </div>
          <button
            type="submit"
            className="confirmButton"
          >
          Submit Employee  
          </button>
          {pendingEERecord && (
            <p>{pendingEERecord.firstName} created successfully! </p>
            )}  
          {employeeSubmissionError && (
            <p>There was an issue submitting the employee. </p>
          )}
        </form>
      </div>
      }
      {currentView === "employeeListView" &&
        <div>
        <h2> All Employee Records </h2>
        {!employeeList && 
          <p>Loading employees... </p>}
         
        <table
          className="employeeTable">
            <thead>
              <tr>
                <th>ID</th>
                <th>Employee Name </th>
                <th>Employee Email Address </th>
                <th>Employee Hire Date </th>
                <th>Job Title </th>
                <th>Department </th>
                <th>Salary </th>
              </tr>
            </thead>
            <tbody>
              {employeeList &&
                employeeList.map((employee) => {
                  return <tr key={employee.id}>
                    <td> {employee.id} </td>
                    <td> {employee.firstName} {employee.lastName} </td>
                    <td> {employee.email} </td>
                    <td> {employee.hireDate} </td>
                    <td> {employee.jobTitle} </td>
                    <td> {employee.department?.departmentName} </td>
                    <td> {employee.salary} </td>
                  </tr> 
                })
              }
            </tbody>
          </table>
        </div>
        }
      </main>
    </>
  )
}

export default App
