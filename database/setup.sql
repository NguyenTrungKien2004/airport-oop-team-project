USE master;
GO
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'AirportManagement')
BEGIN
    ALTER DATABASE AirportManagement SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE AirportManagement;
END
GO
CREATE DATABASE AirportManagement;
GO
USE AirportManagement;
GO

CREATE TABLE Roles (
    RoleID INT PRIMARY KEY,
    RoleName NVARCHAR(50) NOT NULL
);

CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    FullName NVARCHAR(100),
    RoleID INT FOREIGN KEY REFERENCES Roles(RoleID)
);

CREATE TABLE Flights (
    FlightID INT PRIMARY KEY IDENTITY(1,1),
    FlightNumber VARCHAR(20) UNIQUE NOT NULL,
    DepartureCity NVARCHAR(100) NOT NULL,
    DestinationCity NVARCHAR(100) NOT NULL,
    DepartureTime DATETIME NOT NULL,
    ArrivalTime DATETIME,
    Gate VARCHAR(10),
    TotalSeats INT DEFAULT 100,
    Status NVARCHAR(50) DEFAULT N'Scheduled'
);

CREATE TABLE Tickets (
    TicketID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    FlightID INT NOT NULL,
    BookingDate DATETIME DEFAULT GETDATE(),
    SeatNumber VARCHAR(10) NULL,
    Status NVARCHAR(50) DEFAULT N'Confirmed',
    CONSTRAINT FK_Ticket_User FOREIGN KEY (UserID) REFERENCES Users(UserID),
    CONSTRAINT FK_Ticket_Flight FOREIGN KEY (FlightID) REFERENCES Flights(FlightID)
);

INSERT INTO Roles VALUES (1, N'Quản trị viên'), (2, N'Nhân viên'), (3, N'Hành khách');
INSERT INTO Users (Username, Password, FullName, RoleID) VALUES 
('admin', '123', N'Nguyễn Trung Kiên (Admin)', 1),
('staff01', '123', N'Nguyễn Thành Tiến', 2),
('khach01', '123', N'Ngô Hoài Nam', 3),
('duy_passenger', '123', N'Vũ Văn Duy', 3);

INSERT INTO Flights (FlightNumber, DepartureCity, DestinationCity, DepartureTime, ArrivalTime, Gate, TotalSeats, Status) 
VALUES ('VN123', N'Hà Nội', N'Sài Gòn', '2026-05-10 08:00:00', '2026-05-10 10:00:00', 'G1', 100, N'Scheduled');