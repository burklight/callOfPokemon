package utilities.communications;

/**
 * Interface with static variables and others
 */
public interface Comms {

    public static final int windowWidth = 700;
    public static final int windowHeight = 700;
    public static final int buttomWidth = 140;
    public static final int buttomHeigth = 140;
    public static final int obstacleWidth = 35;
    public static final int obstacleHeight = 35;
    public static final int obstacleRows = windowHeight / obstacleHeight;
    public static final int obstacleColumns = windowWidth / obstacleWidth;
    public static final int numObstacles = 6;

    public static final int numCharacters = 22;

    public static final String[] characters = {
        "Bulbasur",
        "Venusaur",
        "Charmander",
        "Charizard",
        "Squirtle",
        "Blastoise",
        "Pikachu",
        "Raichu",
        "BulbasurShiny",
        "VenusaurShiny",
        "CharmanderShiny",
        "CharizardShiny",
        "SquirtleShiny",
        "BlastoiseShiny",
        "PikachuShiny",
        "RaichuShiny",
        "Accelerator",
        "Nanoha",
        "Negi",
        "Zero",
        "Link",
        "Haruhi", //"Marcel"
    };

    //attack damages and others
    public static final int largeAttackDamage = -10;
    public static final int shortAttackDamage = -15;

    //pokemon directions
    public static final int up = 2;
    public static final int right = 1;
    public static final int down = -1;
    public static final int left = -2;

    //inputs
    public static final int changeUp = 1;
    public static final int changeRight = 2;
    public static final int changeDown = 3;
    public static final int changeLeft = 4;
    public static final int move = 5;
    public static final int largeAttack = 6;
    public static final int shortAttack = 7;

    //types of the characters and attacks
    public static final int neutral = -1;
    public static final int rock = 1;
    public static final int electric = 3;
    public static final int water = 4;
    public static final int grass = 5;
    public static final int fire = 2;
    public static final int magic = 6;
    public static final int esper = 7;
    public static final int god = 999;
    public static final int heal = 8;

    //different maps types
    public static final int field = 0;
    public static final int cave = 1;
    public static final int mountain = 2;
    public static final int forest = 3;
    public static final int numMaps = 4;

    //related to powerups (not implemented)
    /*public static int delayAppearPowerUpRandom = 60000; //yes, 1 minute
     public static int delayAppearPowerUpOffset = 30000;
     public static int delayDisappearPowerUpRandom = 5000;
     public static int delayDisappearPowerUpOffset = 15000;
     public static int numPowerUpsInTheMap = 3;
     public static int numPowerUps = 3;*/
    //delay between each movement of the pokemon
    public static long delayChangeDirection = 100;
    public static long delayCharacterAnimation = 200;

    //delay between each movement of the attaks
    public static long minDelaySpeedMovement = 80;
    public static long delayShowLargeAttack = 100;
    public static long delayShowShortAttack = 100;

    //server stuff
    public static final int serverPort = 5454;
    public static final String host = "localhost";
    public static final int SIZE_BUFFER = 30000;

    //keys
    public static final int selectedCharacter = 314;
    public static final int exit = -1;
    public static final int NUM_PLAYERS = 2;
    public static final int numScoresGeneral = 10;
    public static final int numScoresUser = 5;
    public static final int NUM_LARGE_ATTACKS_IN_SCREEN = 100;
    public static final int NUM_SHORT_ATTACKS_IN_SCREEN = 50;
    public static final int MovementQueueCapacity = 20;
    
    public static final int SendMap = 1;
    public static final int AcceptedConnection = 2;
    public static final int DeniedConnection = 3;
    public static final int EndConnection = 4;
    public static final int StringData = 5;
    public static final int InitialPosition = 6;
    public static final int MoveCharacter = 7;
    public static final int CreateNewLargeAttack = 8;
    public static final int MoveLargeAttack = 9;
    public static final int EndLargeAttack = 10;
    public static final int IsLargeAttackOn = 11;
    public static final int ReSendLargeAttack = 12;
    public static final int NewCharacterLife = 13;
    public static final int CreateNewShortAttack = 14;
    public static final int EndShortAttack = 15;
    public static final int ChangeDirection = 16;
    public static final int SendCharacter = 17;
    public static final int CharacterDeath = 18;
    public static final int LinkStart = 19; //now this is SAO
    public static final int CharacterSelected = 20;
    public static final int RegisterNewUser = 21;
    public static final int LinkStartUser = 22;
    public static final int UserRegistered = 23;
    public static final int NoName = 24;
    public static final int WrongPassword = 25;
    public static final int StartGameRequest = 26;
    public static final int WatchRecordsRequest = 27;
    public static final int WatchOldGamesRequest = 28;
    public static final int WatchRecords = 29;
    public static final int WatchOldGames = 30;
    public static final int WaitForAGame = 31;
    public static final int NoGamesAvaliable = 32;
    public static final int StartGame = 33;
    public static final int GameEnded = 34;
    public static final int SendScoresRequest = 35;
    public static final int GameWinned = 36;
    public static final int GameLost = 37;
    public static final int ExistingUser = 38;
    public static final int Instructions = 39;
    public static final int ChangePassword = 40;
    public static final int PasswordChanged = 41;
    public static final int Error = 42;
    public static final int RemoveRecord = 43;
    public static final int SavedRecord = 44;
    public static final int ErrorInSaving = 45;

    //Instruction message
    public static final String GameInstructions
            = "Instructions:\n\n"
            + "The game consists in a " + Comms.NUM_PLAYERS + " players battle royal in a randomly created map.\n"
            + "Each character has its own characteristics which make him able to pass through certain obstacles.\n"
            + "Some powerful characters need of a certain condition to be unlocked.\n\n";
    public static final String GameControls
            = "Controls:\n\n"
            + "Movement: a,w,s,d (left, up, down, right)\n"
            + "Long Attack: y \n"
            + "Short Attack: u \n";
}
