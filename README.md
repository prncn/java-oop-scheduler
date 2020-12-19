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
  - [ ] *Encrypt* login data in database   
- [ ] Admin permission to access, edit and delete users  

### Create Meetings/Appointments
- [ ] User feature to add entries (meetings/appointments)
- [ ] Entry contains info about *event* (see event entity)
- [ ] Entry contains participants (list of *users*)
- [ ] Entry contains *priority* (high, medium, low)
  - [ ] Highlight events based to priority  
- [ ] *Reminder* functionality
  - [ ] Drop down menu to select time
  - [ ] Email notification 

### Delete and Edit Meetings/Appointments
- [ ] Delete from client and database
- [ ] Edit in client and adjust database
- [ ] User and Participant get *email notifcation* on delete/edit

### Documentation
- [ ] Include *Javadoc* as API for classes, methods, attributes
- [ ] *Diagrams* (class, sequence, flow) describing the software architecture and functionality
- [ ] *Project documentation* (max. 18 pages) (see requirements)