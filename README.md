# Java OOP WS20/21 Project
Scheduling application.

## Checklist
### Workflow
- [x] Setup github repo  
- [x] Decide java stack
  - [x] GUI framework: *Swing*
  - [x] Database: *MySQL(Server)*, SQLite(Local Embedded)

### User Profiles 
- [x] Create user profiles and login (authentication) 
  - [x] Provide GUI interface 
  - [x] *Store* login data in database   
  - [x] *Encrypt* login data in database *CONTROLLER*  
- [ ] Admin permission to access, edit and delete users  
  - [x] Admin account placeholder
  - [ ] Design admin panel
  - [ ] Implement accessor, editor and deletor in admin panel

### UI
- [x] Create navigation panels
- [x] Admin panel visibility
- [ ] Dashboard
  - [ ] Fetch user's meeting
- [x] Create Meeting
  - [x] Schedule form
  - [x] Success panel
- [x] View Calendar
  - [x] Monthly display mode
    - [x] Design monthly calendar
    - [x] Correct date information
    - [ ] Display meetings on days
  - [ ] Weekly display mode
    - [x] Design weekly calendar
    - [ ] Display meetings at time

### Create Meetings/Appointments
- [ ] Model meetings system
  - [x] Implement prototype systems (version 2)
  - [ ] Reduce and minimise model
- [x] User UI form to add entries (meetings/appointments)
- [ ] Create and pass Meeting entity as java class
  - [x] Entry contains info about *event* (see event entity)
  - [x] Entry contains participants (list of *users*)
  - [x] Entry contains *priority* (high, medium, low)
    - [ ] Highlight events based to priority  
  - [ ] Pass Meeting to participants *CONTROLLER*
  - [ ] Prepare to display created meetings on dashboards
- [ ] Store meetings entity in database
  - [ ] Link to user / participant
  - [ ] Prepare to display stored meetings on dashboards
- [ ] *Reminder* functionality
  - [x] Drop down menu to select time
  - [ ] Email notification *CONTROLLER*  

### Delete and Edit Meetings/Appointments
- [ ] Delete from client and database
- [ ] Edit in client and adjust database
- [ ] User and Participant get *email notifcation* on delete/edit

### Documentation
- [ ] Include *Javadoc* as API for classes, methods, attributes
- [ ] *Diagrams* (class, sequence, flow) describing the software architecture and functionality
- [ ] *Project documentation* (max. 18 pages) (see requirements)
  - [ ] Description
  - [ ] Motivation
  - [ ] Requirements
  - [ ] Organisation/Task distribution
  - [ ] Conclusion