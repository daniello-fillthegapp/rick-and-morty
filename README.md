# Rick and Morty Android App

This Android app is built around **Clean Architecture**, using **Jetpack Compose** and modern Android libraries.

## Architecture Overview
- **Clean Architecture**: Divided into layers for presentation, domain, and data.
- **Jetpack Compose** and **Material 3**: For building the UI.
- **State Management**: Handled by **StateFlow** and **LiveData**.

### Presentation Layer
- ViewModels handle UI-related data.
- Compose is used to create responsive UI components.

### Domain Layer
- Contains business logic and use cases for interaction with the repository layer.

### Data Layer
- Uses **Room** and local storage for local persistence.
- Uses **Retrofit** for API calls.
- **Repository Layer** abstracts data sources combining them.

## Dependency Injection (DI)
- **Hilt** for DI.
- Provides dependencies for Network, Data Sources, Repositories.
- Provides dependencies for Retrofit, Room, and ViewModels.

## Persistence with Room
- **Room** is used for local data storage.
- Media data is downloaded for offline use.
- **CharacterDao** defines the database operations.

## AndroidX Navigation
- This project uses **AndroidX Navigation** to manage app navigation.
- `NavController` and `NavHost` components are employed to navigate between different screens.

## Project Structure
```
├── data
│   ├── local
│   ├── remote
│   └── repository
├── domain
│   ├── usecase
│   └── model
├── presentation
│   ├── screen
│   ├── ui
│   └── viewmodel
└── utils
```
## Testing
The **data layer** of this project is thoroughly tested using **unit tests** to ensure the correctness and reliability of the data handling components such as repositories, data sources, and network operations.

## How to Run
1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app.

## Conclusion
The project follows Clean Architecture, providing a scalable, testable, and maintainable structure.
