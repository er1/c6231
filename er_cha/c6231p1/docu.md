COMP 6231
====

Eric Chan (9365079)

Distributed Police Information System
----

In this project we were asked to produce a distributed police information system.

This project is distributed in three levels.

The class `StationServer` handles all of the activities of this server and exposes them via RMI so that the `OfficerClient` class can access its methods. `StationServer` is instantiated for as many police stations as needed (in this case 3).

The `StationServer` class communicates with other stations via UDP to other stations via a simple request serialization scheme. The requests and the responses are just key value string pairs. The request consists of a request and a response. Responses are handled in their own thread. The UDP port is based on a preagreed hashing function of the station name so other stations can be added as needed.

There are a minimum two running processes with several threads.

One process for the RMI servers each with a thread for each station. Each station forks an additional thread for its UDP server for inter-station requests.

The `StationServer` class is instantiated for each server, this class then creates a thread to listen to UDP requests.

Records are stored as `Record` objects which is extended into `CriminalRecord` objects for criminal records and `MissingRecord` objects for missing person records.

`RecordContainer` contains all `Record`s in a protected field called records. records is an array containin 26 `HashMap`s of `Record`s keyed on `String`s which are the IDs of the records.

When records are added (or potentially removed) the corresponding map for that record is locked with Java's `synchronized` mechanism. Similarly, this is used to lock the whole records array when making the count of records on a particular server.

When we are reading or modifying records, we lock them on the specific `Record` with `synchronized` so it is in a consistent state.

`StationServer` implements the `StationInterface` for RMI. `StationInterface` extends Java's RMI `Remote` Interface so that it may be exported via RMI to officers or clients in `OfficerClient`

### Web Service

The Web Service aspect of the application is exposed through a WSDL file (`WebServiceAdapter.wsdl`) which works as a lookup for the client to make SOAP requests. SOAP requests are just XML documents used as request response pairs which are sent over HTTP.

`WebServiceAdapter` is an adapter class the adapts SOAP calls into RMI calls by instatiating an RMI Client for each request where a new object is created.

Because of the nature of the Java Web Service, objects on the server side are not persisted, but rather instantiated on demand. For this reason, RMI was used on the server side to make requests to the `StationServer` class so that it would persist for the duration of the application.
