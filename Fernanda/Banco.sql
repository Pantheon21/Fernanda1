CREATE TABLE Pacientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE Medicos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    especialidade VARCHAR(255) NOT NULL
);

CREATE TABLE Consultas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_paciente INT,
    id_medico INT,
    data_hora DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_paciente) REFERENCES Pacientes(id),
    FOREIGN KEY (id_medico) REFERENCES Medicos(id)
);
