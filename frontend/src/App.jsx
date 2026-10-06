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
  const [selectedEmployee, changeSelectedEmployee] = useState(null)
  const [increasePercentage, updateIncreasePercentage] = useState({})
  const [increaseAmount, updateIncreaseAmount] = useState({})
  const increasePercentageRegEx = /^\d*\.?\d?$/
  const increaseAmountRegEx = /^\d*\.?\d{0,2}$/

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
  function formatSalary(salary){
    return salary !== null ? salary.toLocaleString("en-US", {style: "currency", currency: "USD"}): "-"
  }
  function formatDate(date){
   return date != null ? new Date(date).toLocaleDateString(): "-"
  }
  function calculateNewSalary(employee) { 
    if (employee.salary != null) {
      return Math.round((employee.salary * (1 + Number(increasePercentage[employee.id] ?? "0.0") / 100) * 100))/100 
    }
    return null
  }
  function calculateAmountIncrease(employee, percentage){
    if (employee.salary != null) {
      return Math.round((employee.salary * percentage / 100)*100)/100
    }
  }
  function calculatePercentageIncrease(employee, amount){
    if (employee.salary != null) {
      return Math.round((amount  / employee.salary * 100) * 10) / 10
    }
  }

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
          <div className="employeeListContainer">
          {!employeeList && 
            <p>Loading employees... </p>}
          
          <table
            className="employeeTable">
              <thead>
                <tr>
                  <th>ID</th>
                  <th className="employeeTableDataColumn">Employee Name</th>
                  <th className="employeeTableDataColumn">Employee Email Address</th>
                  <th>Employee Hire Date</th>
                  <th className="employeeTableDataColumn">Job Title</th>
                  <th>Department</th>
                  <th>Salary</th>
                  <th>% Increase</th>
                  <th>Amount Increase</th>
                  <th className="tableProposedSalary">New Proposed Salary</th>
                </tr>
              </thead>
              <tbody>
                {employeeList &&
                  employeeList.map((employee) => {
                    return <tr 
                    className={employee.id === selectedEmployee?.id ? "selectedEmployeeRow" : ""}
                    key={employee.id}
                    onClick={() => changeSelectedEmployee(employee)}
                    >
                      <td> {employee.id} </td>
                      <td className="employeeTableDataColumn"> {employee.firstName} {employee.lastName} </td>
                      <td className="employeeTableEmail"> {employee.email} </td>
                      <td> {formatDate(employee.hireDate)} </td>
                      <td className="employeeTableDataColumn"> {employee.jobTitle} </td>
                      <td> {employee.department?.departmentName} </td>
                      <td> {formatSalary(employee.salary)} </td>
                      <td className="percentageIncreaseInput"><input 
                      type="number" 
                      className="percentageIncreaseInput"
                      step="0.1" 
                      inputmode="decimal" 
                      value={increasePercentage[employee.id]?? "0.0"} 
                      onChange={(event) => {
                        if (increasePercentageRegEx.test(event.target.value)){
                          updateIncreasePercentage((previous) => {
                            return {
                              ...previous,
                              [employee.id]:event.target.value}})
                          updateIncreaseAmount((previous) => {
                              return {
                                ...previous,
                                [employee.id]: calculateAmountIncrease(employee, event.target.value)
                              }
                            })    
                        }}}
                      onBlur={(event) => {
                        if (!event.target.value.includes(".")){
                           updateIncreasePercentage((previous) => {
                            return {
                              ...previous,
                              [employee.id]: Number(event.target.value).toFixed(1)
                            }})
                      }}}
                      />
                      <span className="percentageSymbol"> % </span>
                      </td>
                      <td> <input 
                      type="number"
                      className="amountIncreaseInput"
                      step=".01"
                      inputmode="decimal"
                      value={increaseAmount[employee.id]?? "0.00"}
                      onChange={(event) => {
                        if (increaseAmountRegEx.test(event.target.value)){
                          updateIncreaseAmount((previous) => {
                            return {
                              ...previous,
                              [employee.id]:event.target.value}})
                          updateIncreasePercentage((previous) => {
                            return {
                              ...previous,
                              [employee.id]:calculatePercentageIncrease(employee, event.target.value)
                            }})
                          }  
                          }}
                      /> 
                      </td>
                      <td> {formatSalary(calculateNewSalary(employee))} </td>
                    </tr> 
                  })
                }
              </tbody>
            </table>           
          </div>
          {selectedEmployee && 
            <div className="employeeDetailsPane"> 
              <h3> {selectedEmployee.firstName} {selectedEmployee.lastName} </h3>
              <p className="detailsPaneEmail"><span className="detailsPaneLabels"> Email: </span>{selectedEmployee.email} </p>
              <p><span className="detailsPaneLabels"> Hire Date: </span>{formatDate(selectedEmployee.hireDate)} </p>
              <p><span className="detailsPaneLabels"> Job Title: </span>{selectedEmployee.jobTitle} </p>
              <p><span className="detailsPaneLabels"> Department: </span>{selectedEmployee.department?.departmentName} </p>
              <p><span className="detailsPaneLabels"> Salary: </span>{formatSalary(selectedEmployee.salary)} </p>
            <button 
              type="button"
              className="closeDetailsButton"
              onClick={() => changeSelectedEmployee(null)}
            >
            x  
            </button>
            </div>
          }
        </div>
      }
      </main>
    </>
  )
}
export default App