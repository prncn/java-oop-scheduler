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
  - [x] Design admin panel
  - [ ] Implement accessor, editor and deletor in admin panel

### UI
- [x] Create navigation panels
- [x] Admin panel visibility
- [ ] Dashboard
  - [x] Fetch user's meeting
  - [ ] Add clock
  - [ ] Add invites panel
  - [ ] Replace events list with cards
- [x] Create Meeting
  - [x] Schedule form
    - [ ] Location suggestions
    - [ ] Search users suggestions
  - [x] Success panel
- [x] View Calendar
  - [x] Monthly display mode
    - [x] Design monthly calendar
    - [x] Correct date information
    - [x] Display meetings on days
  - [x] Weekly display mode
    - [x] Design weekly calendar
    - [x] Display meetings at time
- [ ] Profile / Admin
  - [ ] Display user data
  - [ ] Edit user data panel
  - [ ] Location creator panel

### Create Meetings/Appointments
- [ ] Model meetings system (*version 3*)
  - [x] Implement prototype systems (version 3)
  - [x] Reduce and minimise model
  - [x] Redesign invite system
- [x] User UI form to add entries (meetings/appointments)
- [x] Create and pass Meeting entity as java class
  - [x] Entry contains info about *event* (see event entity)
  - [x] Entry contains participants (list of *users*)
  - [x] Entry contains *priority* (high, medium, low)
    - [x] Highlight events based to priority  
  - [ ] Pass Meeting to participants *CONTROLLER*
  - [x] Prepare to display created meetings on dashboards
- [ ] Store meetings entity in database
  - [ ] Link to user / participant
  - [ ] Prepare to display stored meetings on dashboards
- [ ] *Reminder* functionality
  - [x] Drop down menu to select time
  - [ ] Email notification *CONTROLLER*  

### Delete and Edit Meetings/Appointments
- [ ] Delete event
  - [ ] From client
  - [ ] From database
- [ ] Edit event
  - [ ] From client
  - [ ] From database
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