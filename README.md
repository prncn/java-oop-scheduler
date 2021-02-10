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
- [x] Admin permission to access, edit and delete users  
  - [x] Admin account placeholder
  - [x] Design admin panel
  - [x] Implement accessor, editor and deletor in admin panel

### UI
- [x] Create navigation panels
- [x] Admin panel visibility
- [x] Dashboard
  - [x] Fetch user's meeting
  - [x] Add clock
  - [x] Add invites panel
  - [x] Replace events list with cards
- [x] Create Meeting
  - [x] Schedule form
    - [x] Location suggestions
    - [x] Search users suggestions
  - [x] Success panel
- [x] View Calendar
  - [x] Monthly display mode
    - [x] Design monthly calendar
    - [x] Correct date information
    - [x] Display meetings on days
  - [x] Weekly display mode
    - [x] Design weekly calendar
    - [x] Display meetings at time
- [x] Profile / Admin
  - [x] Display user data
  - [x] Edit user data panel
  - [x] Location creator panel

### Create Meetings/Appointments
- [x] Model meetings system (*version 3*)
  - [x] Implement prototype systems (version 3)
  - [x] Reduce and minimise model
  - [x] Redesign invite system
- [x] User UI form to add entries (meetings/appointments)
- [x] Create and pass Meeting entity as java class
  - [x] Entry contains info about *event* (see event entity)
  - [x] Entry contains participants (list of *users*)
  - [x] Entry contains *priority* (high, medium, low)
    - [x] Highlight events based to priority  
  - [x] Pass Meeting to participants
  - [x] Prepare to display created meetings on dashboards
- [x] Store meetings entity in database
  - [x] Link to user / participant
  - [x] Prepare to display stored meetings on dashboards
- [x] *Reminder* functionality
  - [x] Drop down menu to select time
  - [x] Email notification *CONTROLLER*  

### Delete and Edit Meetings/Appointments
- [x] Delete event
  - [x] In client
  - [x] In database
- [x] Edit event
  - [x] In client
  - [x] In database
- [x] User and Participant get *email notifcation* on delete/edit

### Documentation
- [x] Include *Javadoc* as API for classes, methods, attributes
- [x] *Diagrams* (class, sequence, flow) describing the software architecture and functionality
- [ ] *Project documentation* (max. 18 pages) (see requirements)
  - [x] Description
  - [x] Motivation
  - [x] Requirements
  - [x] Organisation/Task distribution
  - [x] Technical descriptions and solutions
  - [ ] Conclusion
