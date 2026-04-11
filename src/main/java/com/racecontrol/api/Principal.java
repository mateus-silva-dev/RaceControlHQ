package com.racecontrol.api;

import com.racecontrol.api.league.League;
import com.racecontrol.api.season.Season;
import com.racecontrol.api.season.SeasonStatus;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;

public class Principal {
    public static void main(String[] args) {
        Clock clockReal = Clock.systemDefaultZone();
        League league = new League(
                "GT World Challenger 2026",
                "Novo regulamento. Novas Histórias"
        );

        Season season = new Season(
                "Season 2026",
                LocalDate.of(2026, Month.AUGUST, 1),
                LocalDate.of(2026, Month.AUGUST, 31),
                league,
                clockReal
        );

        System.out.println("= = = = = = LEAGUE = = = = = =");
        testLeague(league);

        System.out.println("= = = = = = SEASON = = = = = =");
        testSeason(season);

        System.out.println("= = = = = = RACE = = = = = =");

    }

    public static void testLeague(League league) {
        showLeague(league);
        league.updateLogoUrl("https://logo.com/logo.png");
        showLeague(league);
        league.updateRulesPdfUrl("https://pdf.com/rules.pdf");
        showLeague(league);

        System.out.println("Atualizando descrição da liga...");
        league.updateDescription("Uma nova descrição.");
        showLeague(league);

        System.out.println("Atualizando nome da liga e slugify...");
        league.updateName("Porsche GT Challenger 2027");
        showLeague(league);

        System.out.println("Atualizando pontos por pole position...");
        league.configureExtraPoints(2, 0);
        showLeague(league);

        System.out.println("Atualizando pontos por volta rápida...");
        league.configureExtraPoints(0, 2);
        showLeague(league);
    }

    public static void showLeague(League league) {
        System.out.printf("""
                        Liga:                       %S
                        Descrição:                  %s
                        Slugify:                    %s
                        Logo da Liga:               %s
                        Livro de Regras:            %s
                        Ponto(s) por Pole Position: %d
                        Ponto(s) por Volta Rápida:  %d
                        Criado em:                  %s
                        """,
                league.getName(),
                league.getDescription(),
                league.getSlugify(),
                league.getLogoUrl(),
                league.getRulesPdfUrl(),
                league.getPointsPole(),
                league.getPointsFastestLap(),
                league.getCreatedAt()
        );
        System.out.println();
    }

    public static void testSeason(Season season) {
        showSeason(season);
        System.out.println("Atualizando titulo da temporada...");
        season.updateTitle("Season 2027");
        showSeason(season);

        System.out.println("Atualizando data de inicio da temporada...");
        season.updateDates(LocalDate.of(2027, Month.DECEMBER, 1), LocalDate.of(2027, Month.DECEMBER, 31), Clock.systemDefaultZone());
        showSeason(season);

        System.out.println("Atualizando status da temporada...");
        season.updateStatus(SeasonStatus.IN_PROGRESS);
        showSeason(season);
    }

    public static void showSeason(Season season) {
        System.out.printf("""
                        Liga:               %s
                        Título:             %s
                        Data de Inicío:     %s
                        Data de Fim:        %s
                        Status:             %s
                        """,
                season.getLeague().getName(),
                season.getTitle(),
                season.getStartDate(),
                season.getEndDate(),
                season.getStatus()
        );
        System.out.println();
    }
}
